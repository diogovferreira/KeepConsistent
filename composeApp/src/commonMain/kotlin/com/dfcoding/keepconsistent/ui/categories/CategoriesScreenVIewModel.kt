package com.dfcoding.keepconsistent.ui.categories

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dfcoding.keepconsistent.data.repository.TaskRepository
import com.dfcoding.keepconsistent.models.CategoriesType
import com.dfcoding.keepconsistent.models.TaskModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CategoriesScreenState {
    data object Loading : CategoriesScreenState
    data class Success(val tasksByCategory: Map<CategoriesType, List<TaskModel>>) : CategoriesScreenState
    data class Error(val message: String) : CategoriesScreenState
}

class CategoriesScreenViewModel(private val taskRepository: TaskRepository) : ScreenModel {

    private val _state = MutableStateFlow<CategoriesScreenState>(CategoriesScreenState.Loading)
    val state: StateFlow<CategoriesScreenState> = _state.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        screenModelScope.launch {
            _state.value = CategoriesScreenState.Loading
            try {
                val tasks = taskRepository.getAllTasks()
                _state.value = CategoriesScreenState.Success(
                    tasks.groupBy { it.categoryType }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = CategoriesScreenState.Error(e.message ?: "Failed to load tasks")
            }
        }
    }
}