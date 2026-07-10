package com.dfcoding.keepconsistent.ui.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dfcoding.keepconsistent.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class LoginViewModel(val authRepository: AuthRepository) : ScreenModel {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUser = authRepository.currentUser.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        null
    )

    fun signIn(email: String, password: String) {
        screenModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authRepository.signInWithEmail(email, password)
                .onSuccess {
                    _uiState.value = AuthUiState.Success
                }.onFailure {
                    _uiState.value = AuthUiState.Error(it.message ?: "Unknown error")
                }
        }
    }

    fun signOut() {
        screenModelScope.launch {
            authRepository.signOut()
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

}