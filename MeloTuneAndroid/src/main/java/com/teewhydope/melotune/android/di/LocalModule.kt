package com.teewhydope.melotune.android.di

import com.teewhydope.melotune.BaseApplication
import com.teewhydope.melotune.datasource.local.song.SongLocalService
import com.teewhydope.melotune.datasource.local.song.SongLocalServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Singleton
    @Provides
    fun provideSongLocalService(
        application: BaseApplication
    ): SongLocalService {
        return SongLocalServiceImpl(
            application = application
        )
    }
}