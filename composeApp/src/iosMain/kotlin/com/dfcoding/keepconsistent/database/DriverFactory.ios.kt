package com.dfcoding.keepconsistent.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = ConsistentDatabase.Schema,
            name = "Consistent.db"
        )
    }
}