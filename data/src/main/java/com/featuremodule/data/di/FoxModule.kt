package com.featuremodule.data.di

import com.featuremodule.data.floof.FoxRepository
import com.featuremodule.data.floof.FoxRepositoryImpl
import com.featuremodule.data.floof.FoxService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class FoxModule {
    @Provides
    internal fun provideFoxService(retrofit: Retrofit) = retrofit.create(FoxService::class.java)

    @Provides
    internal fun provideFoxRepository(foxRepositoryImpl: FoxRepositoryImpl): FoxRepository =
        foxRepositoryImpl
}
