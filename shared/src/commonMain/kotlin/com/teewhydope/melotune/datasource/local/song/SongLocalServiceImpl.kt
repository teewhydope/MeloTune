package com.teewhydope.melotune.datasource.local.song

import com.teewhydope.melotune.domain.model.Song

expect class SongLocalServiceImpl : SongLocalService {
    override suspend fun search(): List<Song>
}