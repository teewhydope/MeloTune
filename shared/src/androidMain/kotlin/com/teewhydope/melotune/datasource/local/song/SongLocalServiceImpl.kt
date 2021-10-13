package com.teewhydope.melotune.datasource.local.song

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.teewhydope.melotune.domain.model.Song
import com.teewhydope.melotune.domain.util.DatetimeUtil

actual class SongLocalServiceImpl(private val application: Context) : SongLocalService {
    actual override suspend fun search(): List<Song> {
        val audioList = mutableListOf<Song>()
        val mContentResolver = application.contentResolver
        val datetimeUtil = DatetimeUtil()

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            //Audio
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Audio.Media.RELATIVE_PATH else
                MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DATE_ADDED,
            //Album
            MediaStore.Audio.Albums.ALBUM_ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            //MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            )

        // Display Audios in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val query = mContentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder
        )

        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media.DISPLAY_NAME,
            )
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val titleColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val dataColumn = cursor.getColumnIndexOrThrow(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Audio.Media.RELATIVE_PATH else
                MediaStore.Audio.Media.DATA)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM,)
            val albumArtistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)
            //val numberOfSongsColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS)



            while (cursor.moveToNext()) {
                // Get values of columns for a given Audio.
                val id = cursor.getLong(idColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val displayName = cursor.getString(displayNameColumn)
                val artist = cursor.getString(artistColumn)
                val title = cursor.getString(titleColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val data = cursor.getString(dataColumn)
                val dateAdded = cursor.getString(dateAddedColumn)
                val album = cursor.getString(albumColumn)
                val albumArtist = cursor.getString(albumArtistColumn)
                //val numberOfSongs = cursor.getInt(numberOfSongsColumn)


                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                val sArtworkUri = Uri.parse("content://media/external/audio/albumart")

                val artWorkUri = ContentUris.withAppendedId(
                    sArtworkUri,
                    albumId,
                )

                audioList += Song(
                    id,
                    contentUri,
                    artWorkUri,
                    displayName,
                    artist,
                    title,
                    duration,
                    size,
                    data,
                    datetimeUtil.toLocalDate(dateAdded.toDouble()),

                )

            }

        }
        return audioList
    }

}