package com.dfcoding.keepconsistent

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.dfcoding.keepconsistent.ui.onboard.OnBoardScreen
import com.dfcoding.optcg.navigation.RootScreen
import com.theme.KeepConsistentTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    KeepConsistentTheme {
        Navigator(OnBoardScreen())
        //Navigator(RootScreen())
    }
}