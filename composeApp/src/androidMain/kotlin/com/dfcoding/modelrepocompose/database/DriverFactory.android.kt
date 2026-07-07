package com.dfcoding.modelrepocompose.database

import android.content.Context


actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
 /*       return AndroidSqliteDriver(
            schema = TcgDatabase.Schema,
            context = context,
            name = "Tcg.db"
        )*/
    }
}
