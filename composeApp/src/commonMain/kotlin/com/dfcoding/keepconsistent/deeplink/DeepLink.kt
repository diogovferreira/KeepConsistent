package com.dfcoding.keepconsistent.deeplink

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PasswordRecoveryState {
    private val _isPasswordRecovery = MutableStateFlow(false)
    val isPasswordRecovery: StateFlow<Boolean> = _isPasswordRecovery

    fun markRecovery() { _isPasswordRecovery.value = true }
    fun consume() { _isPasswordRecovery.value = false }
}

fun isRecoveryLink(url: String?): Boolean = url?.contains("type=recovery") == true
