package com.dfcoding.keepconsistent.data.repository

import com.dfcoding.keepconsistent.data.auth.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<AuthUser?>

    suspend fun signUpWithEmail(email: String, password: String): Result<Unit>
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
}