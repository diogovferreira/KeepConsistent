package com.dfcoding.keepconsistent.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.dfcoding.keepconsistent.ui.categories.CategoriesScreen
import com.dfcoding.keepconsistent.ui.components.AppBottomBar
import com.dfcoding.keepconsistent.ui.components.BottomNavItem
import com.dfcoding.keepconsistent.ui.home.HomeScreen
import com.dfcoding.keepconsistent.ui.profile.ProfileScreen
import com.dfcoding.keepconsistent.ui.schedule.ScheduleScreen
import modelrepocompose.composeapp.generated.resources.Res
import modelrepocompose.composeapp.generated.resources.ic_calendar
import modelrepocompose.composeapp.generated.resources.ic_home
import modelrepocompose.composeapp.generated.resources.ic_messages
import modelrepocompose.composeapp.generated.resources.ic_profile

class RootScreen : Screen {
    @Composable
    override fun Content() {
        var currentTab by remember { mutableStateOf("home") }

        val homeScreen = remember { HomeScreen() }
        val scheduleScreen = remember { ScheduleScreen() }
        val categoriesScreen = remember { CategoriesScreen() }
        val profileScreen = remember { ProfileScreen() }

        val tabs = remember {
            listOf(
                BottomNavItem(Res.drawable.ic_home, "home"),
                BottomNavItem(Res.drawable.ic_calendar, "schedule"),
                BottomNavItem(Res.drawable.ic_messages, "categories"),
                BottomNavItem(Res.drawable.ic_profile, "profile"),
            )
        }



        Scaffold(
            bottomBar = {
                AppBottomBar(
                    items = tabs,
                    selectedIndex = tabs.indexOfFirst { it.label == currentTab }.coerceAtLeast(0),
                    onItemSelected = {index -> currentTab = tabs[index].label},
                    onFabClick = { }
                )
            }
        ){ paddingValues ->
            Box(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
                when (currentTab) {
                    "home" -> homeScreen.Content()
                    "schedule" -> scheduleScreen.Content()
                    "categories" -> categoriesScreen.Content()
                    "profile" -> profileScreen.Content()

                }
            }
        }
    }
}