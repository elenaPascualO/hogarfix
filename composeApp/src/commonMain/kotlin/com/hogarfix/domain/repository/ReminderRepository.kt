package com.hogarfix.domain.repository

import com.hogarfix.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getAllActive(): Flow<List<Reminder>>
    fun getOverdue(): Flow<List<Reminder>>
    fun getUpcoming(daysAhead: Int): Flow<List<Reminder>>
    fun getByHomeItem(homeItemId: Long): Flow<List<Reminder>>
    fun searchByText(query: String): Flow<List<Reminder>>
    suspend fun getById(id: Long): Reminder?
    suspend fun save(reminder: Reminder): Long
    suspend fun delete(id: Long)
    suspend fun complete(id: Long)
}
