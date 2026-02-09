package com.hogarfix.ui.screens.reminders

import com.hogarfix.domain.model.Category
import kotlinx.datetime.LocalDate

enum class IntervalUnit(val label: String, val toDays: (Int) -> Int) {
    DAYS("dias", { it }),
    WEEKS("semanas", { it * 7 }),
    MONTHS("meses", { it * 30 }),
    YEARS("anos", { it * 365 });
}

data class ReminderFormState(
    val id: Long? = null,
    val title: String = "",
    val description: String = "",
    val category: Category? = null,
    val intervalValue: Int = 1,
    val intervalUnit: IntervalUnit = IntervalUnit.MONTHS,
    val nextDueDate: LocalDate? = null,
    val homeItemId: Long? = null,
    val isActive: Boolean = true,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val showDatePicker: Boolean = false,
    val error: String? = null
) {
    val isEditMode: Boolean get() = id != null

    val isValid: Boolean
        get() = title.isNotBlank() && category != null && intervalValue > 0 && nextDueDate != null

    val intervalDays: Int
        get() = intervalUnit.toDays(intervalValue)
}
