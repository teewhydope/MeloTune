package com.teewhydope.melotune.interactors.song_list

import com.teewhydope.melotune.datasource.cache.song.SongCache
import com.teewhydope.melotune.datasource.local.song.SongLocalService
import com.teewhydope.melotune.domain.model.GenericMessageInfo
import com.teewhydope.melotune.domain.model.Song
import com.teewhydope.melotune.domain.model.UIComponentType
import com.teewhydope.melotune.domain.util.CommonFlow
import com.teewhydope.melotune.domain.util.DataState
import com.teewhydope.melotune.domain.util.asCommonFlow
import com.teewhydope.melotune.util.Logger
import kotlinx.coroutines.flow.flow


class SearchLocalSongs(
    private val songCache: SongCache,
    private val songLocalService: SongLocalService
) {
    private val logger = Logger("SearchLocalSongs")

    fun execute(
        query: String,
    ): CommonFlow<DataState<List<Song>>> = flow {
        try {
            emit(DataState.loading())
            val songs = songLocalService.search()
            // insert into cache
            songCache.insert(songs)
            // query the cache
            val cacheResult = if (query.isBlank()) {
                songCache.getAll()
            } else {
                songCache.search(
                    query = query,
                )
            }
            // emit List<Song> from cache
            emit(DataState.data(message = null, data = cacheResult))
        } catch (e: Exception) {
            emit(
                DataState.error(
                    message = GenericMessageInfo.Builder()
                        .id("SearchSongs.Error")
                        .title("Error")
                        .uiComponentType(UIComponentType.Dialog)
                        .description(e.message ?: "Unknown Error")
                )
            )
        }
    }.asCommonFlow()

}
