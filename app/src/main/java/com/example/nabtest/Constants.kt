package com.example.nabtest

enum class LoadingState {
    LOADING,
    REFRESHING,
    SUCCESS,
    ERROR
}

const val SHARED_PREFS_NAME = "SharePrefs"