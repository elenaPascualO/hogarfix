package com.hogarfix.ui.screens.professionals

import com.hogarfix.domain.model.Category

data class ProfessionalFormState(
    val id: Long? = null,
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val specialty: Category? = null,
    val personalRating: Int? = null,
    val notes: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val error: String? = null
) {
    val isEditMode: Boolean get() = id != null

    val isValid: Boolean
        get() = name.isNotBlank() && specialty != null
}
