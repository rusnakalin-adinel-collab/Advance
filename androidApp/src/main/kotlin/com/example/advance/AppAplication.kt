package com.example.advance

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import ua.edu.chnu.kkn.advancedkotlinmultiplatform.di.initKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AppApplication)
            androidLogger()
        }
    }
}