package com.hogarfix.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

data class Reminder(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val intervalDays: Int,
    val nextDueDate: LocalDate,
    val homeItemId: Long? = null,
    val category: Category,
    val isActive: Boolean = true,
    val lastCompletedDate: LocalDate? = null,
    val createdAt: Instant
)