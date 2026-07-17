package com.dfcoding.keepconsistent.ui.updatepassword

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dfcoding.keepconsistent.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UpdatePasswordUiState {
    data object Idle : UpdatePasswordUiState
    data object Loading : UpdatePasswordUiState
    data object Success : UpdatePasswordUiState
    data class Error(val message: String) : UpdatePasswordUiState
}

class UpdatePasswordScreenViewModel(val authRepository: AuthRepository): ScreenModel {
    private val _uiState = MutableStateFlow<UpdatePasswordUiState>(UpdatePasswordUiState.Idle)
    var uiState: StateFlow<UpdatePasswordUiState> = _uiState.asStateFlow()


    fun updatePassword(newPassword: String) {
        screenModelScope.launch {
            _uiState.value = UpdatePasswordUiState.Loading
            authRepository.updatePassword(newPassword)
                .onSuccess { _uiState.value = UpdatePasswordUiState.Success }
                .onFailure { _uiState.value = UpdatePasswordUiState.Error(it.message ?: "Something went wrong") }
        }
    }

    fun resetState() { _uiState.value = UpdatePasswordUiState.Idle }
}