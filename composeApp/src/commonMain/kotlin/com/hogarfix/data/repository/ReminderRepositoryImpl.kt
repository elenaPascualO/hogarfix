package com.hogarfix.data.repository

import com.hogarfix.data.local.dao.ReminderDao
import com.hogarfix.data.mapper.ReminderMapper
import com.hogarfix.domain.model.Reminder
import com.hogarfix.domain.repository.ReminderRepository
import com.hogarfix.util.NotificationScheduler
import com.hogarfix.util.currentDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

class ReminderRepositoryImpl(
    private val reminderDao: ReminderDao,
    private val notificationScheduler: NotificationScheduler
) : ReminderRepository {

    override fun getAllActive(): Flow<List<Reminder>> {
        return reminderDao.getAllActive().map { entities ->
            entities.map { ReminderMapper.toDomain(it) }
        }
    }

    override fun getOverdue(): Flow<List<Reminder>> {
        val today = currentDate()
        return reminderDao.getOverdue(today.toEpochDays().toLong()).map { entities ->
            entities.map { ReminderMapper.toDomain(it) }
        }
    }

    override fun getUpcoming(daysAhead: Int): Flow<List<Reminder>> {
        val today = currentDate()
        val futureDate = today.plus(daysAhead, DateTimeUnit.DAY)
        return reminderDao.getUpcoming(
            today = today.toEpochDays().toLong(),
            futureDate = futureDate.toEpochDays().toLong()
        ).map { entities ->
            entities.map { ReminderMapper.toDomain(it) }
        }
    }

    override fun getByHomeItem(homeItemId: Long): Flow<List<Reminder>> {
        return reminderDao.getByHomeItem(homeItemId).map { entities ->
            entities.map { ReminderMapper.toDomain(it) }
        }
    }

    override fun searchByText(query: String): Flow<List<Reminder>> {
        return reminderDao.searchByText(query).map { entities ->
            entities.map { ReminderMapper.toDomain(it) }
        }
    }

    override suspend fun getById(id: Long): Reminder? {
        return reminderDao.getById(id)?.let { ReminderMapper.toDomain(it) }
    }

    override suspend fun save(reminder: Reminder): Long {
        val entity = ReminderMapper.toEntity(reminder)
        val id = if (reminder.id == 0L) {
            reminderDao.insert(entity)
        } else {
            reminderDao.update(entity)
            reminder.id
        }
        val savedReminder = reminder.copy(id = id)
        if (savedReminder.isActive) {
            notificationScheduler.schedule(savedReminder)
        } else {
            notificationScheduler.cancel(id)
        }
        return id
    }

    override suspend fun delete(id: Long) {
        notificationScheduler.cancel(id)
        reminderDao.deleteById(id)
    }

    override suspend fun complete(id: Long) {
        val entity = reminderDao.getById(id) ?: return
        val reminder = ReminderMapper.toDomain(entity)
        val today = currentDate()
        val updatedReminder = reminder.copy(
            lastCompletedDate = today,
            nextDueDate = today.plus(reminder.intervalDays, DateTimeUnit.DAY)
        )
        reminderDao.update(ReminderMapper.toEntity(updatedReminder))
        notificationScheduler.schedule(updatedReminder)
    }
}
