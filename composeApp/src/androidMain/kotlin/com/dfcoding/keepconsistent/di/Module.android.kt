package com.dfcoding.keepconsistent.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.dfcoding.keepconsistent.database.DriverFactory
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<Settings> {
        SharedPreferencesSettings(
            androidContext().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        )
    }
    single { DriverFactory(androidContext()) }
    single<SqlDriver> { get<DriverFactory>().createDriver() }
}