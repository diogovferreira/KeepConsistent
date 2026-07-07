package com.dfcoding.modelrepocompose

import androidx.compose.ui.window.ComposeUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.dfcoding.modelrepocompose.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }