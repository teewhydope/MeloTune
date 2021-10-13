package com.teewhydope.melotune.datasource.local.song

import com.teewhydope.melotune.domain.model.Song

actual class SongLocalServiceImpl : SongLocalService {
    actual override suspend fun search(): List<Song> {
        TODO("Not yet implemented")
    }
}