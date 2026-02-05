package com.hogarfix.ui.screens.interventions

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.DoneBy
import com.hogarfix.domain.model.Status
import kotlinx.datetime.LocalDate

sealed interface InterventionFormEvent {
    data class TitleChanged(val title: String) : InterventionFormEvent
    data class DescriptionChanged(val description: String) : InterventionFormEvent
    data class DateChanged(val date: LocalDate) : InterventionFormEvent
    data class CategoryChanged(val category: Category) : InterventionFormEvent
    data class LaborCostChanged(val cost: String) : InterventionFormEvent
    data class MaterialCostChanged(val cost: String) : InterventionFormEvent
    data class StatusChanged(val status: Status) : InterventionFormEvent
    data class DoneByChanged(val doneBy: DoneBy) : InterventionFormEvent
    data class NotesChanged(val notes: String) : InterventionFormEvent
    data class PhotoAdded(val photoBytes: ByteArray) : InterventionFormEvent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            other as PhotoAdded
            return photoBytes.contentEquals(other.photoBytes)
        }
        override fun hashCode(): Int = photoBytes.contentHashCode()
    }
    data class PhotoRemoved(val uri: String) : InterventionFormEvent
    data class PendingPhotoRemoved(val index: Int) : InterventionFormEvent
    data object ShowDatePicker : InterventionFormEvent
    data object HideDatePicker : InterventionFormEvent
    data object Save : InterventionFormEvent
    data object ClearError : InterventionFormEvent
}
