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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dfcoding.keepconsistent.ui.addtask.AddTaskScreen
import com.dfcoding.keepconsistent.ui.categories.CategoriesScreen
import com.dfcoding.keepconsistent.ui.components.AppBottomBar
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
        val categoriesScreen = remember { CategoriesScreen() }

        val tabs = remember {
            listOf(
                BottomNavItem(Res.drawable.ic_home, "home"),
                BottomNavItem(Res.drawable.ic_messages, "categories"),
            )
        }



        Scaffold(
            bottomBar = {
                AppBottomBar(
                    items = tabs,
                    selectedIndex = tabs.indexOfFirst { it.label == currentTab }.coerceAtLeast(0),
                    onItemSelected = {index -> currentTab = tabs[index].label},
                    onFabClick = { navigator.push(AddTaskScreen()) }
                )
            }
        ){ paddingValues ->
            Box(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
                when (currentTab) {
                    "home" -> homeScreen.Content()
                    "categories" -> categoriesScreen.Content()
                }
            }
        }
    }
}