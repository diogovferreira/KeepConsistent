package com.dfcoding.keepconsistent.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenStateless()
    }
}


@Composable
fun HomeScreenStateless(){
    Text("Home")
}