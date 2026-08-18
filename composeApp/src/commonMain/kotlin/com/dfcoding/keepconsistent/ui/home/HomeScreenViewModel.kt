package com.dfcoding.keepconsistent.ui.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dfcoding.keepconsistent.data.repository.TaskRepository
import com.dfcoding.keepconsistent.models.Frequency
import com.dfcoding.keepconsistent.models.TaskModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

sealed interface HomeScreenState {
    data object Loading : HomeScreenState
    data class Success(val tasks: List<TaskModel>) : HomeScreenState
    data class Error(val message: String) : HomeScreenState
}

class HomeScreenViewModel(private val taskRepository: TaskRepository) : ScreenModel {

    private val timeZone = TimeZone.currentSystemDefault()

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    init {
        loadTodayTasks()
    }

    // Also what a notification tap should trigger — reloads today's due tasks
    // and their current completed/not-completed status.
    @OptIn(ExperimentalTime::class)
    fun loadTodayTasks() {
        screenModelScope.launch {
            _state.value = HomeScreenState.Loading
            try {
                val today = kotlin.time.Clock.System.todayIn(timeZone)
                val todayTasks = taskRepository.getAllTasks().filter { isDueToday(it, today) }
                _state.value = HomeScreenState.Success(todayTasks)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = HomeScreenState.Error(e.message ?: "Failed to load tasks")
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun isCompletedToday(task: TaskModel): Boolean {
        val today = kotlin.time.Clock.System.todayIn(timeZone).toEpochDays().toInt()
        return task.lastCompletedEpochDay == today
    }

    @OptIn(ExperimentalTime::class)
    fun toggleComplete(task: TaskModel) {
        val id = task.id ?: return
        if (isCompletedToday(task)) return // no "undo" yet, just guards a redundant call

        screenModelScope.launch {
            try {
                val today = kotlin.time.Clock.System.todayIn(timeZone)
                taskRepository.completeTask(
                    taskId = id,
                    epochDay = today.toEpochDays().toInt(),
                    completedAtMillis = kotlin.time.Clock.System.now().toEpochMilliseconds()
                )
                loadTodayTasks()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = HomeScreenState.Error(e.message ?: "Failed to complete task")
            }
        }
    }

    fun deleteTask(task: TaskModel) {
        val id = task.id ?: return
        screenModelScope.launch {
            try {
                taskRepository.deleteTask(id)
                loadTodayTasks()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = HomeScreenState.Error(e.message ?: "Failed to delete task")
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun isDueToday(task: TaskModel, today: LocalDate): Boolean = when (task.frequency) {
        Frequency.Daily -> true
        Frequency.Weekly -> task.listOfWeekDays.orEmpty()
            .any { it.equals(today.dayOfWeek.name, ignoreCase = true) }
        Frequency.Monthly -> task.listOfMonthDays.orEmpty().contains(today.dayOfMonth)
        Frequency.Custom -> task.customDate?.let {
            Instant.fromEpochMilliseconds(it).toLocalDateTime(timeZone).date == today
        } == true
    }
}