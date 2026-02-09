package com.hogarfix.ui.screens.professionals

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Professional

sealed interface ProfessionalListEvent {
    data object AddProfessional : ProfessionalListEvent
    data class EditProfessional(val professional: Professional) : ProfessionalListEvent
    data class RequestDelete(val professional: Professional) : ProfessionalListEvent
    data object ConfirmDelete : ProfessionalListEvent
    data object CancelDelete : ProfessionalListEvent
    data class FilterByCategory(val category: Category?) : ProfessionalListEvent
    data object ClearError : ProfessionalListEvent
}
