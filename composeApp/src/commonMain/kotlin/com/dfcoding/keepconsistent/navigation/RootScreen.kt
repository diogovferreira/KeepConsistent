package com.dfcoding.keepconsistent.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dfcoding.keepconsistent.ui.components.BottomNavItem
import com.dfcoding.keepconsistent.ui.home.HomeScreen
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_home
import keepconsistent.composeapp.generated.resources.ic_messages

class RootScreen : Screen {
    @Composable
    override fun Content() {
        var currentTab by remember { mutableStateOf("home") }
        val navigator = LocalNavigator.currentOrThrow


        val homeScreen = remember { HomeScreen() }

        val tabs = remember {
            listOf(
                BottomNavItem(Res.drawable.ic_home, "home"),
            )
        }




    }
}