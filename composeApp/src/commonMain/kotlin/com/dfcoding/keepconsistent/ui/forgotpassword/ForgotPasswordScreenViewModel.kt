package com.dfcoding.keepconsistent.ui.forgotpassword

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dfcoding.keepconsistent.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ForgotPasswordUiState {
    data object Idle : ForgotPasswordUiState
    data object Loading : ForgotPasswordUiState
    data object Success : ForgotPasswordUiState
    data class Error(val message: String) : ForgotPasswordUiState
}

class ForgotPasswordScreenViewModel(val authRepository: AuthRepository) : ScreenModel {
    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    var uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()


    fun recoverPassword(email: String) {
        screenModelScope.launch {
            _uiState.value = ForgotPasswordUiState.Loading
            authRepository.sendPasswordReset(email)
                .onSuccess {
                    _uiState.value = ForgotPasswordUiState.Success
                }.onFailure {
                    _uiState.value = ForgotPasswordUiState.Error(it.message ?: "Unknown error")
                }
        }
    }


    fun resetState() { _uiState.value = ForgotPasswordUiState.Idle }

}
