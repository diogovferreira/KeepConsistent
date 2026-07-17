package com.dfcoding.keepconsistent.data.repository

import com.dfcoding.keepconsistent.data.auth.AuthUser
import kotlinx.coroutines.flow.Flow


sealed interface SessionState{
    data object Loading: SessionState
    data object LoggedOut: SessionState
    data class LoggedIn(val user: AuthUser) : SessionState
}
interface AuthRepository {
    val sessionState: Flow<SessionState>
    val currentUser: Flow<AuthUser?>
    suspend fun signUpWithEmail(email: String, password: String): Result<Unit>
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>
}