package com.dfcoding.keepconsistent.models

import androidx.compose.ui.graphics.Color
import keepconsistent.composeapp.generated.resources.Res
import keepconsistent.composeapp.generated.resources.ic_break
import keepconsistent.composeapp.generated.resources.ic_megaphone
import keepconsistent.composeapp.generated.resources.ic_personal
import keepconsistent.composeapp.generated.resources.ic_work
import org.jetbrains.compose.resources.DrawableResource

data class CategoryModel(
    val category: CategoriesType,
    val tasks: Int
)


enum class CategoriesType(val icon: DrawableResource) {
    Personal(Res.drawable.ic_personal),
    Work(Res.drawable.ic_work),
    Break(Res.drawable.ic_break)
}


fun CategoriesType.accentColor(): Color = when (this) {
    CategoriesType.Personal -> Color(0xFF246ECF)
     CategoriesType.Break -> Color(0xFFB3261E)
    CategoriesType.Work -> Color(0xFF5DB975)
}