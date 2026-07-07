package com.dfcoding.modelrepocompose

import android.app.Application
import com.dfcoding.modelrepocompose.di.appModules
import com.dfcoding.modelrepocompose.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TestApp : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TestApp)
            modules(platformModule + appModules)
        }
    }
}

