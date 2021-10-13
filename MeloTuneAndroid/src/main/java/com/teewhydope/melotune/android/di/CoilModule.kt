package com.teewhydope.melotune.android.di

import android.app.Application
import coil.ImageLoader
import com.teewhydope.melotune.android.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Hilt Coil Module Setup

@InstallIn(SingletonComponent::class)
@Module
object CoilModule {
    @Provides
    @Singleton
    fun provideImageLoader(app: Application): ImageLoader{
        return ImageLoader.Builder(app)
            .error(R.drawable.music)
            .placeholder(R.drawable.music)
            .availableMemoryPercentage(0.25) // Don't know what is recommended?
            .crossfade(true)
            .build()
    }
}