package com.hogarfix.ui.screens.interventions

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.DoneBy
import com.hogarfix.domain.model.Professional
import com.hogarfix.domain.model.Status
import kotlinx.datetime.LocalDate

data class InterventionFormState(
    val id: Long? = null,
    val title: String = "",
    val description: String = "",
    val date: LocalDate? = null,
    val category: Category? = null,
    val laborCost: String = "",
    val materialCost: String = "",
    val status: Status = Status.PENDING,
    val doneBy: DoneBy = DoneBy.MYSELF,
    val professionalId: Long? = null,
    val professionals: List<Professional> = emptyList(),
    val homeItemId: Long? = null,
    val photoUris: List<String> = emptyList(),
    val pendingPhotos: List<ByteArray> = emptyList(),
    val notes: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val error: String? = null,
    val showDatePicker: Boolean = false
) {
    val isEditMode: Boolean get() = id != null

    val isValid: Boolean
        get() = title.isNotBlank() && category != null && date != null

    val laborCostDouble: Double?
        get() = laborCost.toDoubleOrNull()

    val materialCostDouble: Double?
        get() = materialCost.toDoubleOrNull()

    val selectedProfessional: Professional?
        get() = professionalId?.let { id -> professionals.find { it.id == id } }
}
