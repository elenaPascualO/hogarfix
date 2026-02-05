package com.hogarfix.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.hogarfix.data.local.dao.HomeItemDao
import com.hogarfix.data.local.dao.InterventionDao
import com.hogarfix.data.local.dao.ProfessionalDao
import com.hogarfix.data.local.dao.ReminderDao
import com.hogarfix.data.local.entity.HomeItemEntity
import com.hogarfix.data.local.entity.InterventionEntity
import com.hogarfix.data.local.entity.ProfessionalEntity
import com.hogarfix.data.local.entity.ReminderEntity

@Database(
    entities = [
        InterventionEntity::class,
        HomeItemEntity::class,
        ProfessionalEntity::class,
        ReminderEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interventionDao(): InterventionDao
    abstract fun homeItemDao(): HomeItemDao
    abstract fun professionalDao(): ProfessionalDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        const val DATABASE_NAME = "hogarfix.db"
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
