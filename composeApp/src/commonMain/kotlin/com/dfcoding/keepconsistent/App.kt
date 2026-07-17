package com.dfcoding.keepconsistent

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.dfcoding.keepconsistent.ui.AppEntryScreen
import com.dfcoding.keepconsistent.di.appModules
import com.dfcoding.keepconsistent.di.platformModule
import com.theme.KeepConsistentTheme
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration

@Composable
fun App(additionalKoinConfig: KoinAppDeclaration = {}) {
    KoinApplication(application = {
        additionalKoinConfig()
        modules(appModules + platformModule)
    }) {
        HandleAuthDeepLink()
        KeepConsistentTheme {
            Navigator(AppEntryScreen())
        }
    }
}