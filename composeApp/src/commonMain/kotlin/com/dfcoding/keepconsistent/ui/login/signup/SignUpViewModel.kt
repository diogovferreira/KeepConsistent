package com.dfcoding.keepconsistent.ui.login.signup

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dfcoding.keepconsistent.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SignUpUiState {
    data object Idle : SignUpUiState
    data object Loading : SignUpUiState
    data object Success : SignUpUiState
    data class Error(val message: String) : SignUpUiState
}

class SignUpViewModel(private val authRepository: AuthRepository) : ScreenModel {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()


    fun signUp(email: String, password: String) {
        screenModelScope.launch {
            _uiState.value = SignUpUiState.Loading
            authRepository.signUpWithEmail(email, password)
                .onSuccess {
                    _uiState.value = SignUpUiState.Success
                }
                .onFailure {
                    _uiState.value = SignUpUiState.Error(it.message ?: "Unknown error")
                }
        }
    }

    fun resetState() {
        _uiState.value = SignUpUiState.Idle
    }
}