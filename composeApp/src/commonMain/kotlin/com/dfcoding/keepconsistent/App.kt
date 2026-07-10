package com.dfcoding.keepconsistent

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.dfcoding.keepconsistent.ui.onboard.OnBoardScreen
import com.dfcoding.modelrepocompose.di.appModules
import com.dfcoding.modelrepocompose.di.platformModule
import com.theme.KeepConsistentTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModules + platformModule)
    }) {
        KeepConsistentTheme {
            Navigator(OnBoardScreen())
        }
    }
}