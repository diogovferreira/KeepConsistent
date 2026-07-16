package com.dfcoding.keepconsistent.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dfcoding.keepconsistent.data.repository.AuthRepository
import com.dfcoding.keepconsistent.data.repository.OnBoardingRepository
import com.dfcoding.keepconsistent.data.repository.SessionState
import com.dfcoding.keepconsistent.navigation.RootScreen
import com.dfcoding.keepconsistent.ui.components.LoadingComponent
import com.dfcoding.keepconsistent.ui.login.LoginScreen
import com.dfcoding.keepconsistent.ui.onboard.OnBoardScreen
import org.koin.compose.koinInject

class AppEntryScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val authRepository = koinInject<AuthRepository>()
        val onBoardingRepository = koinInject<OnBoardingRepository>()
        val sessionState by authRepository.sessionState.collectAsState(initial = SessionState.Loading)

        LaunchedEffect(sessionState){
            when(val state = sessionState){
                SessionState.Loading -> {}
                is SessionState.LoggedIn -> navigator.replaceAll(RootScreen())
                SessionState.LoggedOut -> {
                    if (onBoardingRepository.hasSeenOnboard()) {
                        navigator.replaceAll(LoginScreen())
                    } else {
                        navigator.replaceAll(OnBoardScreen())
                    }
                }
            }
        }

        LoadingComponent()
    }
}