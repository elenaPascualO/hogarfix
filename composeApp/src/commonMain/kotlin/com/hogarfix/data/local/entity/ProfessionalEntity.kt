package com.hogarfix.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "professionals")
data class ProfessionalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String?,
    val email: String?,
    val specialty: String,
    val personalRating: Int?,
    val notes: String?,
    val createdAt: Long // Instant as epoch millis
)