package com.dfcoding.keepconsistent.models

import androidx.compose.ui.graphics.Color
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_megaphone
import org.jetbrains.compose.resources.DrawableResource

data class CategoryModel(
    val category: CategoriesType,
)


enum class CategoriesType(val icon: DrawableResource) {
    Personal(Res.drawable.ic_megaphone),
    Work(Res.drawable.ic_megaphone),
    Break(Res.drawable.ic_megaphone)
}


fun CategoriesType.accentColor(): Color = when (this) {
    CategoriesType.Personal -> Color(0xFF246ECF)
     CategoriesType.Break -> Color(0xFFB3261E)
    CategoriesType.Work -> Color(0xFF5DB975)
}