package com.dfcoding.optcg.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class RootScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        //val viewModel = getScreenModel<SearchScreenViewModel>()
        var currentTab by remember { mutableStateOf("home") }


        Scaffold(
            bottomBar = {
/*                SearchBottomBar(
                    currentRoute = currentTab,
                    onNavigateSearch = { currentTab = "search" },
                    onNavigateCollection = { currentTab = "collection" },
                )*/
            }
        ){ paddingValues ->
    /*        Box(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
                when (currentTab) {
                    "search" -> SearchScreen().Content()
                    "collection" -> CollectionScreen().Content()
                }
            }*/
        }
    }
}