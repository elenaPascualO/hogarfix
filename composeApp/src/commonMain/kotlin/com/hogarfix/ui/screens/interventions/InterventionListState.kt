package com.hogarfix.ui.screens.interventions

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Intervention

data class InterventionListState(
    val interventions: List<Intervention> = emptyList(),
    val isLoading: Boolean = true,
    val selectedCategory: Category? = null,
    val interventionToDelete: Intervention? = null,
    val error: String? = null
) {
    val filteredInterventions: List<Intervention>
        get() = if (selectedCategory != null) {
            interventions.filter { it.category == selectedCategory }
        } else {
            interventions
        }

    val isEmpty: Boolean
        get() = !isLoading && filteredInterventions.isEmpty()
}
