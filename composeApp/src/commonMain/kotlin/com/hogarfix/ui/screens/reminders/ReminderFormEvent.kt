package com.hogarfix.ui.screens.reminders

import com.hogarfix.domain.model.Category
import kotlinx.datetime.LocalDate

sealed interface ReminderFormEvent {
    data class TitleChanged(val title: String) : ReminderFormEvent
    data class DescriptionChanged(val description: String) : ReminderFormEvent
    data class CategoryChanged(val category: Category) : ReminderFormEvent
    data class IntervalValueChanged(val value: Int) : ReminderFormEvent
    data class IntervalUnitChanged(val unit: IntervalUnit) : ReminderFormEvent
    data class NextDueDateChanged(val date: LocalDate) : ReminderFormEvent
    data class IsActiveChanged(val isActive: Boolean) : ReminderFormEvent
    data object ShowDatePicker : ReminderFormEvent
    data object HideDatePicker : ReminderFormEvent
    data object Save : ReminderFormEvent
    data object RequestDelete : ReminderFormEvent
    data object ConfirmDelete : ReminderFormEvent
    data object CancelDelete : ReminderFormEvent
    data object ClearError : ReminderFormEvent
}
