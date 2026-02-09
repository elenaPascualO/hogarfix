package com.hogarfix.ui.screens.reminders

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Reminder
import com.hogarfix.util.currentDate
import kotlinx.datetime.daysUntil

data class ReminderListState(
    val reminders: List<Reminder> = emptyList(),
    val isLoading: Boolean = true,
    val selectedCategory: Category? = null,
    val reminderToDelete: Reminder? = null,
    val error: String? = null
) {
    val filteredReminders: List<Reminder>
        get() = if (selectedCategory != null) {
            reminders.filter { it.category == selectedCategory }
        } else {
            reminders
        }

    val overdueReminders: List<Reminder>
        get() {
            val today = currentDate()
            return filteredReminders.filter { today.daysUntil(it.nextDueDate) < 0 }
        }

    val upcomingReminders: List<Reminder>
        get() {
            val today = currentDate()
            return filteredReminders.filter {
                val days = today.daysUntil(it.nextDueDate)
                days in 0..7
            }
        }

    val onTrackReminders: List<Reminder>
        get() {
            val today = currentDate()
            return filteredReminders.filter { today.daysUntil(it.nextDueDate) > 7 }
        }

    val isEmpty: Boolean
        get() = !isLoading && filteredReminders.isEmpty()
}
