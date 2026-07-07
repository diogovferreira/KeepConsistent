package com.dfcoding.modelrepocompose.di

import app.cash.sqldelight.db.SqlDriver
import com.dfcoding.optcg.database.DriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> { DriverFactory(androidContext()).createDriver() }
}