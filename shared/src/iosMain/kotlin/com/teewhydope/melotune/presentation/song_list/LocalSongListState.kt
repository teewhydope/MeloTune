package com.teewhydope.melotune.presentation.song_list

import com.teewhydope.melotune.domain.model.GenericMessageInfo
import com.teewhydope.melotune.domain.model.Song
import com.teewhydope.melotune.domain.util.Queue

actual data class LocalSongListState(
    val isLoading: Boolean = false,
    val query: String = "",
    val songs: List<Song> = listOf(),
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()), // messages to be displayed in ui
) {
    // Need secondary constructor to initialize with no args in SwiftUI

    constructor() : this(
        isLoading = false,
        query = "",
        songs = listOf(),
        queue = Queue(mutableListOf()),
    )
}