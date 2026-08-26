package com.dfcoding.keepconsistent.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dfcoding.keepconsistent.data.repository.OnBoardingRepository
import com.dfcoding.keepconsistent.ui.components.LoadingComponent
import com.dfcoding.keepconsistent.ui.home.HomeScreen
import com.dfcoding.keepconsistent.ui.login.onboard.OnBoardScreen
import org.koin.compose.koinInject

class AppEntryScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val onBoardingRepository = koinInject<OnBoardingRepository>()


        LaunchedEffect(Unit){
            if (onBoardingRepository.hasSeenOnboard()) {
                navigator.replaceAll(HomeScreen())
            } else {
                navigator.replaceAll(OnBoardScreen())
            }
        }

        LoadingComponent()
    }
}