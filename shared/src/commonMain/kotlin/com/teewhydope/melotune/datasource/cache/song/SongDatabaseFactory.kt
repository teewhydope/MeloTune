package com.teewhydope.melotune.datasource.cache.song

import android.net.Uri
import com.squareup.sqldelight.db.SqlDriver
import com.teewhydope.melotune.datasource.cache.SongDatabase
import com.teewhydope.melotune.datasource.cache.Song_Entity
import com.teewhydope.melotune.domain.model.Song
import com.teewhydope.melotune.domain.util.DatetimeUtil

class SongDatabaseFactory(
    private val driverFactory: DriverFactory
) {
    fun createDatabase(): SongDatabase {
        return SongDatabase(driverFactory.createDriver())
    }
}

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun Song_Entity.toSong(): Song {
    val datetimeUtil = DatetimeUtil()
    return Song(
        id = id,
        uri = Uri
            .parse(uri),
        albumArt = Uri
            .parse(albumArt),
        displayName = displayName,
        artist = artist,
        title = title,
        duration = duration.toInt(),
        size = size.toInt(),
        data = data_,
        dateAdded = datetimeUtil.toLocalDate(dateAdded),
    )
}

fun List<Song_Entity>.toSongList(): List<Song> {
    return map { it.toSong() }
}


