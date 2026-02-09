package com.hogarfix.domain.model

import kotlinx.datetime.Instant

data class Professional(
    val id: Long = 0,
    val name: String,
    val phone: String? = null,
    val email: String? = null,
    val specialty: Category,
    val personalRating: Int? = null,
    val notes: String? = null,
    val createdAt: Instant
)