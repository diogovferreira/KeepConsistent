package com.dfcoding.keepconsistent

import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dfcoding.keepconsistent.Platform
import com.dfcoding.keepconsistent.deeplink.PasswordRecoveryState
import com.dfcoding.keepconsistent.deeplink.isRecoveryLink
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import org.koin.compose.koinInject

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

object AndroidDeepLinkHolder {
    var pendingIntent by mutableStateOf<Intent?>(null)
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun HandleAuthDeepLink() {
    val supabaseClient = koinInject<SupabaseClient>()
    val intent = AndroidDeepLinkHolder.pendingIntent
    LaunchedEffect(intent) {
        val uriString = intent?.data?.toString()
        println("DeepLink: received uri = $uriString")
        uriString?.let {
            if (isRecoveryLink(it)) {
                println("DeepLink: detected recovery link, marking state")
                PasswordRecoveryState.markRecovery()
            } else {
                println("DeepLink: not a recovery link")
            }
        }
        intent?.let { supabaseClient.handleDeeplinks(it) }
    }
}