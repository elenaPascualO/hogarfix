package com.hogarfix.ui.screens.professionals

import com.hogarfix.domain.model.Category

sealed interface ProfessionalFormEvent {
    data class NameChanged(val name: String) : ProfessionalFormEvent
    data class PhoneChanged(val phone: String) : ProfessionalFormEvent
    data class EmailChanged(val email: String) : ProfessionalFormEvent
    data class SpecialtyChanged(val specialty: Category) : ProfessionalFormEvent
    data class RatingChanged(val rating: Int?) : ProfessionalFormEvent
    data class NotesChanged(val notes: String) : ProfessionalFormEvent
    data object Save : ProfessionalFormEvent
    data object RequestDelete : ProfessionalFormEvent
    data object ConfirmDelete : ProfessionalFormEvent
    data object CancelDelete : ProfessionalFormEvent
    data object ClearError : ProfessionalFormEvent
}
