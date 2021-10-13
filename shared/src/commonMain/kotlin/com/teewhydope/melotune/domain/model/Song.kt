package com.teewhydope.melotune.domain.model

import android.net.Uri
import kotlinx.datetime.LocalDateTime

data class Song(
    val id: Long,
    val uri: Uri,
    val albumArt: Uri?,
    val displayName: String,
    val artist: String?,
    val title: String,
    val duration: Int,
    val size: Int,
    val data: String,
    val dateAdded: LocalDateTime,


)