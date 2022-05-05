package com.example.nabtest.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.nabtest.SHARED_PREFS_NAME
import com.example.nabtest.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSchedulers(): SchedulerProvider = object : SchedulerProvider {
        override val io: Scheduler
            get() = Schedulers.io()

        override val ui: Scheduler
            get() = AndroidSchedulers.mainThread()
    }
}