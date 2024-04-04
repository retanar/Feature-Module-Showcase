package com.featuremodule.core.di

import com.featuremodule.core.navigation.NavManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NavModule {
    @Singleton
    @Provides
    fun provideNavManager() = NavManager()
}
