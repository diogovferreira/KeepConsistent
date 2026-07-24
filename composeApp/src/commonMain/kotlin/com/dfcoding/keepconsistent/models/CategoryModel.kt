package com.dfcoding.keepconsistent.models

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