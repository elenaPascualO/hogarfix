package com.hogarfix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_items")
data class HomeItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val brand: String?,
    val model: String?,
    val category: String,
    val purchaseDate: Long?, // LocalDate as epoch days
    val warrantyEndDate: Long?,
    val location: String?,
    val notes: String?,
    val photoUris: String, // JSON array
    val createdAt: Long, // Instant as epoch millis
    val updatedAt: Long
)