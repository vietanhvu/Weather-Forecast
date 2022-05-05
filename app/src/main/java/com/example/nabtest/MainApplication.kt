package com.example.nabtest

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply()
    }
}