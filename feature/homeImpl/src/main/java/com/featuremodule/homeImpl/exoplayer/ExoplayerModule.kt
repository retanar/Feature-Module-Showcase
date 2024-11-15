package com.featuremodule.homeImpl.exoplayer

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ExoplayerModule {
    @ViewModelScoped
    @Provides
    fun provideExoplayer(@ApplicationContext context: Context) = ExoPlayer.Builder(context).build()
}
