package com.dfcoding.keepconsistent.ui.schedule

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class ScheduleScreen: Screen {
    @Composable
    override fun Content() {
        ScheduleScreenStateless()
    }
}

@Composable
fun ScheduleScreenStateless() {
    Text("Schedule")
}