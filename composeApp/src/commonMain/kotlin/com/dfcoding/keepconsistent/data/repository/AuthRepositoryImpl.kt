package com.dfcoding.keepconsistent.data.repository

import com.dfcoding.keepconsistent.data.auth.AuthUser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(private val client: SupabaseClient) : AuthRepository {


    override val currentUser: Flow<AuthUser?> =
        client.auth.sessionStatus.map { status ->
            (status as? SessionStatus.Authenticated)?.session?.user?.let { user ->
                AuthUser(
                    id = user.id,
                    email = user.email
                )
            }
        }


    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<Unit> =
        runCatching {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        }


    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<Unit> =
        runCatching {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }

    override suspend fun signOut(): Result<Unit> = runCatching {
        client.auth.signOut()
    }
}