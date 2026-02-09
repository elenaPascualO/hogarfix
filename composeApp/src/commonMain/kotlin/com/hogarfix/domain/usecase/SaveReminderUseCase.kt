package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Reminder
import com.hogarfix.domain.repository.ReminderRepository

class SaveReminderUseCase(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(reminder: Reminder): Long =
        repository.save(reminder)
}
