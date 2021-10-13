package com.teewhydope.melotune.datasource.cache.song

import com.teewhydope.melotune.domain.model.Song

interface SongCache {
    fun insert(song: Song)

    fun insert(songs: List<Song>)

    fun search(query: String): List<Song>

    fun getAll(): List<Song>

    @Throws(NullPointerException::class)
    fun get(id: Long): Song?
}