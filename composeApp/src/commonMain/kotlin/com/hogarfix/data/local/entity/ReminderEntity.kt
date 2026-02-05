package com.hogarfix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String?,
    val intervalDays: Int,
    val nextDueDate: Long, // LocalDate as epoch days
    val homeItemId: Long?,
    val category: String,
    val isActive: Boolean,
    val lastCompletedDate: Long?, // LocalDate as epoch days
    val createdAt: Long // Instant as epoch millis
)