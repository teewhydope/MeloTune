package com.teewhydope.melotune.datasource.local.song

import com.teewhydope.melotune.domain.model.Song

interface SongLocalService {
    suspend fun search(): List<Song>
}