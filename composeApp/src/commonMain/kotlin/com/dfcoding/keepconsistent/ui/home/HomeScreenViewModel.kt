package com.dfcoding.keepconsistent.ui.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dfcoding.keepconsistent.data.repository.TaskRepository
import com.dfcoding.keepconsistent.models.CategoryModel
import com.dfcoding.keepconsistent.models.Frequency
import com.dfcoding.keepconsistent.models.TaskModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

sealed interface HomeScreenState {
    data object Loading : HomeScreenState
    data class Success(
        val selectedDate: LocalDate,
        val tasks: List<TaskModel>,
        val completedTaskIds: Set<Long>,
        val categories: List<CategoryModel>
    ) : HomeScreenState

    data class Error(val message: String) : HomeScreenState
}

class HomeScreenViewModel(private val taskRepository: TaskRepository) : ScreenModel {

    private val timeZone = TimeZone.currentSystemDefault()

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    // Called from HomeScreen's Content() on every (re)entry into composition —
    // both the first load and when popping back from AddTaskScreen — so newly
    // added/deleted/completed tasks always show up instead of a stale cache.
    // Reuses the currently selected date if there is one, so returning from
    // AddTaskScreen doesn't silently jump you back to today.
    @OptIn(ExperimentalTime::class)
    fun refresh() {
        val date = (_state.value as? HomeScreenState.Success)?.selectedDate
            ?: kotlin.time.Clock.System.todayIn(timeZone)
        loadTasksFor(date)
    }

    fun selectDate(date: LocalDate) = loadTasksFor(date)

    private fun loadTasksFor(date: LocalDate) {
        screenModelScope.launch {
            _state.value = HomeScreenState.Loading
            try {
                val allTasks = taskRepository.getAllTasks()
                val dueTasks = taskRepository.getAllTasks().filter { isDueOn(it, date) }
                val epochDay = date.toEpochDays().toInt()
                val categories = allTasks
                    .groupingBy { it.categoryType }
                    .eachCount()
                    .map { (category, count) -> CategoryModel(category, count) }
                    .sortedWith(compareByDescending<CategoryModel> { it.tasks }.thenBy { it.category.ordinal })

                val completedIds = dueTasks.mapNotNull { task ->
                    task.id?.takeIf { taskRepository.isCompletedForDay(it, epochDay) }
                }.toSet()
                _state.value = HomeScreenState.Success(date, dueTasks, completedIds, categories)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = HomeScreenState.Error(e.message ?: "Failed to load tasks")
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun toggleComplete(task: TaskModel) {
        val id = task.id ?: return
        val current = _state.value as? HomeScreenState.Success ?: return
        if (id in current.completedTaskIds) return // already completed that day, no "undo" yet

        screenModelScope.launch {
            try {
                taskRepository.completeTask(
                    taskId = id,
                    epochDay = current.selectedDate.toEpochDays().toInt(),
                    completedAtMillis = kotlin.time.Clock.System.now().toEpochMilliseconds()
                )
                loadTasksFor(current.selectedDate)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = HomeScreenState.Error(e.message ?: "Failed to complete task")
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun deleteTask(task: TaskModel) {
        val id = task.id ?: return
        val date = (_state.value as? HomeScreenState.Success)?.selectedDate
            ?: kotlin.time.Clock.System.todayIn(timeZone)

        screenModelScope.launch {
            try {
                taskRepository.deleteTask(id)
                loadTasksFor(date)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = HomeScreenState.Error(e.message ?: "Failed to delete task")
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun isDueOn(task: TaskModel, date: LocalDate): Boolean = when (task.frequency) {
        Frequency.Daily -> true
        Frequency.Weekly -> task.listOfWeekDays.orEmpty().any { it.equals(date.dayOfWeek.name, ignoreCase = true) }
        Frequency.Monthly -> task.listOfMonthDays.orEmpty().contains(date.dayOfMonth)
        Frequency.Custom -> task.customDate?.let {
            Instant.fromEpochMilliseconds(it).toLocalDateTime(timeZone).date == date
        } == true
    }
}