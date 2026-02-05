package com.hogarfix.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

data class HomeItem(
    val id: Long = 0,
    val name: String,
    val brand: String? = null,
    val model: String? = null,
    val category: Category,
    val purchaseDate: LocalDate? = null,
    val warrantyEndDate: LocalDate? = null,
    val location: String? = null,
    val notes: String? = null,
    val photoUris: List<String> = emptyList(),
    val createdAt: Instant,
    val updatedAt: Instant
)