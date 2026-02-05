package com.hogarfix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interventions")
data class InterventionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String?,
    val date: Long, // LocalDate as epoch days
    val category: String,
    val laborCost: Double?,
    val materialCost: Double?,
    val status: String,
    val doneBy: String,
    val professionalId: Long?,
    val homeItemId: Long?,
    val photoUris: String, // JSON array
    val notes: String?,
    val createdAt: Long, // Instant as epoch millis
    val updatedAt: Long
)