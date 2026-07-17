package com.dfcoding.keepconsistent.data.repository

import com.dfcoding.keepconsistent.data.auth.AuthUser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.event.AuthEvent
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(private val client: SupabaseClient) : AuthRepository {

    override val sessionState: Flow<SessionState> = client.auth.sessionStatus.map { status ->
        when(status){
            is SessionStatus.Authenticated -> SessionState.LoggedIn(AuthUser(id = status.session.user?.id.orEmpty(), email = status.session.user?.email))
            SessionStatus.Initializing -> SessionState.Loading
            is SessionStatus.NotAuthenticated -> SessionState.LoggedOut
            is SessionStatus.RefreshFailure -> SessionState.LoggedOut // expired/unrecovered
        }
    }



    override val currentUser: Flow<AuthUser?> = sessionState.map { (it as? SessionState.LoggedIn)?.user }

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

    override suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching{
        client.auth.resetPasswordForEmail(email)
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> = runCatching {
        client.auth.updateUser {
            password = newPassword
        }
    }
}