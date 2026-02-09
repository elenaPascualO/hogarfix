package com.hogarfix.domain.usecase

import com.hogarfix.domain.repository.ReminderRepository

class CompleteReminderUseCase(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(id: Long) = repository.complete(id)
}
