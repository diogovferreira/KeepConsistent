package com.dfcoding.keepconsistent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(additionalKoinConfig = {
                androidContext(this@MainActivity)
            })
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}