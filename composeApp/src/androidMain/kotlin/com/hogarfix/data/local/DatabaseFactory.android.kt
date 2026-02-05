package com.hogarfix.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private var appContext: Context? = null

fun initDatabaseContext(context: Context) {
    appContext = context.applicationContext
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context = appContext ?: throw IllegalStateException("Database context not initialized. Call initDatabaseContext first.")
    val dbFile = context.getDatabasePath(AppDatabase.DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}
