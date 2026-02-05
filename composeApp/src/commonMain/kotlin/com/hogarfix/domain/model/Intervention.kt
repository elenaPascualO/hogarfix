package com.hogarfix.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

data class Intervention(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val date: LocalDate,
    val category: Category,
    val laborCost: Double? = null,
    val materialCost: Double? = null,
    val status: Status = Status.PENDING,
    val doneBy: DoneBy,
    val professionalId: Long? = null,
    val homeItemId: Long? = null,
    val photoUris: List<String> = emptyList(),
    val notes: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    val totalCost: Double
        get() = (laborCost ?: 0.0) + (materialCost ?: 0.0)
}