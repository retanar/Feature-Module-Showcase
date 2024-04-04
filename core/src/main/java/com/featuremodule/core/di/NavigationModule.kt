package com.featuremodule.core.di

import com.featuremodule.core.navigation.NavigationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NavigationModule {
    @Singleton
    @Provides
    fun provideNavigationManager() = NavigationManager()
}
