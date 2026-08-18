package com.dfcoding.keepconsistent.ui.addtask

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dfcoding.keepconsistent.data.repository.TaskRepository
import com.dfcoding.keepconsistent.models.TaskModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AddTaskState {
    data object Idle : AddTaskState
    data object Loading : AddTaskState
    data object Success : AddTaskState
    data class Error(val message: String) : AddTaskState
}

class AddTaskScreenViewModel(private val taskRepository: TaskRepository) : ScreenModel {

    private val _addTaskState = MutableStateFlow<AddTaskState>(AddTaskState.Idle)
    val addTaskState: StateFlow<AddTaskState> = _addTaskState.asStateFlow()

    fun addTask(task: TaskModel) {
        screenModelScope.launch {
            _addTaskState.value = AddTaskState.Loading

            try {
                taskRepository.insertTask(task)
                _addTaskState.value = AddTaskState.Success
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _addTaskState.value = AddTaskState.Error(e.message ?: "Failed to add task")
            }
        }
    }

    // Call after the UI reacts to Success/Error (e.g. navigates away, shows a snackbar),
    // otherwise recomposition/re-collection will replay the same state.
    fun resetState() {
        _addTaskState.value = AddTaskState.Idle
    }
}