package com.teewhydope.melotune.android.di

import com.teewhydope.melotune.BaseApplication
import com.teewhydope.melotune.datasource.cache.*
import com.teewhydope.melotune.datasource.cache.song.SongCache
import com.teewhydope.melotune.datasource.cache.song.SongCacheImpl
import com.teewhydope.melotune.datasource.cache.song.SongDatabaseFactory
import com.teewhydope.melotune.domain.util.DatetimeUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideSongDatabase(context: BaseApplication): SongDatabase {
        return SongDatabaseFactory(driverFactory = com.teewhydope.melotune.datasource.cache.song.DriverFactory(context)).createDatabase()
    }

    @Singleton
    @Provides
    fun provideSongCache(
        songDatabase: SongDatabase,
        datetimeUtil: DatetimeUtil,
    ): SongCache {
        return SongCacheImpl(
            songDatabase = songDatabase,
            datetimeUtil = datetimeUtil,
        )
    }
}