package com.hogarfix.ui.screens.inventory

import com.hogarfix.domain.model.Category
import kotlinx.datetime.LocalDate

data class HomeItemFormState(
    val id: Long? = null,
    val name: String = "",
    val brand: String = "",
    val model: String = "",
    val category: Category? = null,
    val purchaseDate: LocalDate? = null,
    val warrantyEndDate: LocalDate? = null,
    val location: String = "",
    val notes: String = "",
    val photoUris: List<String> = emptyList(),
    val pendingPhotos: List<ByteArray> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val error: String? = null,
    val showPurchaseDatePicker: Boolean = false,
    val showWarrantyDatePicker: Boolean = false
) {
    val isEditMode: Boolean get() = id != null

    val isValid: Boolean
        get() = name.isNotBlank() && category != null
}
