package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Reminder
import com.hogarfix.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow

class GetRemindersUseCase(
    private val repository: ReminderRepository
) {
    operator fun invoke(): Flow<List<Reminder>> = repository.getAllActive()

    fun getOverdue(): Flow<List<Reminder>> = repository.getOverdue()

    fun getUpcoming(daysAhead: Int = 7): Flow<List<Reminder>> =
        repository.getUpcoming(daysAhead)

    fun getByHomeItem(homeItemId: Long): Flow<List<Reminder>> =
        repository.getByHomeItem(homeItemId)

    suspend fun getById(id: Long): Reminder? = repository.getById(id)
}
