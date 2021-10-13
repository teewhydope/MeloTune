package com.teewhydope.melotune.datasource.cache.song

import com.teewhydope.melotune.datasource.cache.SongDatabase
import com.teewhydope.melotune.datasource.cache.SongDbQueries
import com.teewhydope.melotune.domain.model.Song
import com.teewhydope.melotune.domain.util.DatetimeUtil

class SongCacheImpl(
    private val songDatabase: SongDatabase,
    private val datetimeUtil: DatetimeUtil,
) : SongCache {
    private var queries: SongDbQueries = songDatabase.songDbQueries

    override fun insert(song: Song) {
        song.artist?.let {
            queries.insertSong(
                id = song.id,
                uri = song.uri.toString(),
                albumArt = song.albumArt.toString(),
                displayName = song.displayName,
                artist = it,
                title = song.title,
                duration = song.duration.toLong(),
                size = song.size.toLong(),
                data_ = song.data,
                dateAdded = datetimeUtil.toEpochMilliseconds(song.dateAdded),
            )
        }
    }

    override fun insert(songs: List<Song>) {
        for (song in songs) {
            insert(song)
        }
    }

    override fun search(query: String): List<Song> {
        return queries.searchSongs(
            query = query,
        ).executeAsList().toSongList()
    }

    override fun getAll(): List<Song> {
        return queries.getAllSongs(
        ).executeAsList().toSongList()
    }

    override fun get(id: Long): Song? {
        return try {
            queries.getSongById(id = id).executeAsOne().toSong()
        } catch (e: NullPointerException) {
            null
        }
    }
}