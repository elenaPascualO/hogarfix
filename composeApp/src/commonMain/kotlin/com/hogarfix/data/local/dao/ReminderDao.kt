package com.hogarfix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hogarfix.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE isActive = 1 ORDER BY nextDueDate ASC")
    fun getAllActive(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders ORDER BY nextDueDate ASC")
    fun getAll(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getById(id: Long): ReminderEntity?

    @Query("SELECT * FROM reminders WHERE isActive = 1 AND nextDueDate <= :today ORDER BY nextDueDate ASC")
    fun getOverdue(today: Long): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE isActive = 1 AND nextDueDate BETWEEN :today AND :futureDate ORDER BY nextDueDate ASC")
    fun getUpcoming(today: Long, futureDate: Long): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE homeItemId = :itemId ORDER BY nextDueDate ASC")
    fun getByHomeItem(itemId: Long): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: ReminderEntity): Long

    @Update
    suspend fun update(reminder: ReminderEntity)

    @Delete
    suspend fun delete(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteById(id: Long)
}
