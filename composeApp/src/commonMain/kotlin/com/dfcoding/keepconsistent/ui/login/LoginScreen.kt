package com.dfcoding.keepconsistent.ui.login

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.ktor.websocket.Frame

class LoginScreen: Screen {
    @Composable
    override fun Content() {
        LoginScreenStateless()
    }
}

@Composable
fun LoginScreenStateless(){
    Frame.Text("Login")
}