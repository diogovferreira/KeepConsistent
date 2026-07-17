package com.dfcoding.keepconsistent

import androidx.compose.runtime.Composable
import com.dfcoding.keepconsistent.deeplink.PasswordRecoveryState
import com.dfcoding.keepconsistent.deeplink.isRecoveryLink
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import org.koin.mp.KoinPlatform.getKoin
import platform.Foundation.NSURL
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()



fun handleIncomingUrl(url: NSURL) {
    if (isRecoveryLink(url.absoluteString)) PasswordRecoveryState.markRecovery()
    getKoin().get<SupabaseClient>().handleDeeplinks(url)
}

@Composable
actual fun HandleAuthDeepLink() {

}