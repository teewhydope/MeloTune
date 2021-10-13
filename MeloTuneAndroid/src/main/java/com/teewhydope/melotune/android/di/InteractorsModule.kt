package com.teewhydope.melotune.android.di

import com.teewhydope.melotune.datasource.cache.song.SongCache
import com.teewhydope.melotune.datasource.local.song.SongLocalService
import com.teewhydope.melotune.interactors.song_list.SearchLocalSongs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorsModule {

    @Singleton
    @Provides
    fun provideSearchSongs(
        songLocalService: SongLocalService,
        songCache: SongCache,
    ): SearchLocalSongs {
        return SearchLocalSongs(
            songLocalService = songLocalService,
            songCache = songCache
        )
    }
}