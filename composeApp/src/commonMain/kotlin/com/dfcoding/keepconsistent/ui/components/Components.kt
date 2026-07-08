package com.dfcoding.keepconsistent.ui.components

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun ButtonComponent(text: String, icon: String? = null, onClick: () -> Unit){
    Button(onClick = onClick) {

    }
}