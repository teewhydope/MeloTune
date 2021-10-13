package com.teewhydope.melotune.datasource.cache.song

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.teewhydope.melotune.datasource.cache.SongDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SongDatabase.Schema, context, "songs.db")
    }
}