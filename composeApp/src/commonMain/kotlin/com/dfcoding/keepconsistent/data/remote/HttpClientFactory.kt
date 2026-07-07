package com.dfcoding.optcg.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.SIMPLE

fun createHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true  // don't crash on new API fields
            coerceInputValues = true  // null instead of crash on type mismatch
        })
    }
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.BODY        // swap to LogLevel.NONE for release
    }
}