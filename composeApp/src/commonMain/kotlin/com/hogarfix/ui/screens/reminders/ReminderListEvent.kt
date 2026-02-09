package com.hogarfix.ui.screens.reminders

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Reminder

sealed interface ReminderListEvent {
    data object AddReminder : ReminderListEvent
    data class EditReminder(val reminder: Reminder) : ReminderListEvent
    data class CompleteReminder(val id: Long) : ReminderListEvent
    data class RequestDelete(val reminder: Reminder) : ReminderListEvent
    data object ConfirmDelete : ReminderListEvent
    data object CancelDelete : ReminderListEvent
    data class FilterByCategory(val category: Category?) : ReminderListEvent
    data object ClearError : ReminderListEvent
}
