package com.hogarfix.ui.screens.inventory

import com.hogarfix.domain.model.Category
import kotlinx.datetime.LocalDate

sealed interface HomeItemFormEvent {
    data class NameChanged(val name: String) : HomeItemFormEvent
    data class BrandChanged(val brand: String) : HomeItemFormEvent
    data class ModelChanged(val model: String) : HomeItemFormEvent
    data class CategoryChanged(val category: Category) : HomeItemFormEvent
    data class PurchaseDateChanged(val date: LocalDate) : HomeItemFormEvent
    data class WarrantyEndDateChanged(val date: LocalDate) : HomeItemFormEvent
    data class LocationChanged(val location: String) : HomeItemFormEvent
    data class NotesChanged(val notes: String) : HomeItemFormEvent
    data class PhotoAdded(val photoBytes: ByteArray) : HomeItemFormEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            other as PhotoAdded
            return photoBytes.contentEquals(other.photoBytes)
        }
        override fun hashCode(): Int = photoBytes.contentHashCode()
    }
    data class PhotoRemoved(val uri: String) : HomeItemFormEvent
    data class PendingPhotoRemoved(val index: Int) : HomeItemFormEvent
    data object ShowPurchaseDatePicker : HomeItemFormEvent
    data object HidePurchaseDatePicker : HomeItemFormEvent
    data object ShowWarrantyDatePicker : HomeItemFormEvent
    data object HideWarrantyDatePicker : HomeItemFormEvent
    data object ClearPurchaseDate : HomeItemFormEvent
    data object ClearWarrantyDate : HomeItemFormEvent
    data object Save : HomeItemFormEvent
    data object RequestDelete : HomeItemFormEvent
    data object ConfirmDelete : HomeItemFormEvent
    data object CancelDelete : HomeItemFormEvent
    data object ClearError : HomeItemFormEvent
}
