package com.hogarfix.domain.usecase

import com.hogarfix.domain.repository.ReminderRepository

class DeleteReminderUseCase(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
