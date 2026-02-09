package com.hogarfix.ui.screens.professionals

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Professional

data class ProfessionalListState(
    val professionals: List<Professional> = emptyList(),
    val interventionCounts: Map<Long, Int> = emptyMap(),
    val isLoading: Boolean = true,
    val selectedCategory: Category? = null,
    val professionalToDelete: Professional? = null,
    val error: String? = null
) {
    val filteredProfessionals: List<Professional>
        get() = if (selectedCategory != null) {
            professionals.filter { it.specialty == selectedCategory }
        } else {
            professionals
        }

    val isEmpty: Boolean
        get() = !isLoading && filteredProfessionals.isEmpty()

    fun getInterventionCount(professionalId: Long): Int =
        interventionCounts[professionalId] ?: 0
}
