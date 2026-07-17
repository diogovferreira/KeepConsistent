package com.dfcoding.keepconsistent

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidDeepLinkHolder.pendingIntent = intent

        setContent {
            App(additionalKoinConfig = {
                androidContext(this@MainActivity)
            })
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        AndroidDeepLinkHolder.pendingIntent = intent
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}