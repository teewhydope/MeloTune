package com.teewhydope.melotune.presentation.song_list

sealed class LocalSongListEvents {
    object LoadSongs : LocalSongListEvents()

    object NewSearch : LocalSongListEvents()

    data class OnUpdateQuery(val query: String) : LocalSongListEvents()

    object OnRemoveHeadMessageFromQueue : LocalSongListEvents()
}