package com.github.sample

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GitHubApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}