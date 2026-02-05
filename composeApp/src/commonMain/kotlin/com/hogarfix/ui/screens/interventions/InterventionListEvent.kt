package com.hogarfix.ui.screens.interventions

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Intervention

sealed interface InterventionListEvent {
    data object AddIntervention : InterventionListEvent
    data class EditIntervention(val intervention: Intervention) : InterventionListEvent
    data class RequestDelete(val intervention: Intervention) : InterventionListEvent
    data object ConfirmDelete : InterventionListEvent
    data object CancelDelete : InterventionListEvent
    data class FilterByCategory(val category: Category?) : InterventionListEvent
    data object ClearError : InterventionListEvent
}
