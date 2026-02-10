package com.hogarfix

import android.app.Application
import com.hogarfix.data.local.initDatabaseContext
import com.hogarfix.data.storage.initPhotoStorageContext
import com.hogarfix.di.initKoin
import com.hogarfix.util.initAppPreferencesContext
import com.hogarfix.util.initNotificationContext
import com.hogarfix.util.initPlatformActionsContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize database context
        initDatabaseContext(this)

        // Initialize photo storage context
        initPhotoStorageContext(this)

        // Initialize platform actions context
        initPlatformActionsContext(this)

        // Initialize app preferences context
        initAppPreferencesContext(this)

        // Initialize notification context
        initNotificationContext(this)

        // Initialize Koin
        initKoin {
            androidLogger()
            androidContext(this@MainApplication)
        }
    }
}
