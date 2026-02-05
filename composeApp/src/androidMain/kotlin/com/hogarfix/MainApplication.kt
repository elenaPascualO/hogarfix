package com.hogarfix

import android.app.Application
import com.hogarfix.data.local.initDatabaseContext
import com.hogarfix.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize database context
        initDatabaseContext(this)

        // Initialize Koin
        initKoin {
            androidLogger()
            androidContext(this@MainApplication)
        }
    }
}
