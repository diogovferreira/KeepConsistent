package com.dfcoding.keepconsistent.ui.categories

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class CategoriesScreen: Screen {
    @Composable
    override fun Content() {
        CategoriesScreenStateless()
    }
}

@Composable
fun CategoriesScreenStateless() {
    Text("Categories")
}
