package com.example.nabtest.di

import android.content.SharedPreferences
import com.example.nabtest.api.ApiService
import com.example.nabtest.repositories.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesRemoteDataSource(apiService: ApiService): RemoteDataSource =
        RemoteDataSourceImpl(apiService)

    @Singleton
    @Provides
    fun providesLocalDataSource(sharedPreferences: SharedPreferences): LocalDataSource = LocalDataSourceImpl(sharedPreferences)

    @Singleton
    @Provides
    fun providesForecastRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): ForecastRepository = ForecastRepositoryImpl(remoteDataSource, localDataSource)
}