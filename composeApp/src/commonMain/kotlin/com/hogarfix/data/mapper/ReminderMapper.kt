package com.hogarfix.data.mapper

import com.hogarfix.data.local.entity.ReminderEntity
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Reminder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

object ReminderMapper {

    fun toDomain(entity: ReminderEntity): Reminder {
        return Reminder(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            intervalDays = entity.intervalDays,
            nextDueDate = LocalDate.fromEpochDays(entity.nextDueDate.toInt()),
            homeItemId = entity.homeItemId,
            category = Category.valueOf(entity.category),
            isActive = entity.isActive,
            lastCompletedDate = entity.lastCompletedDate?.let { LocalDate.fromEpochDays(it.toInt()) },
            createdAt = Instant.fromEpochMilliseconds(entity.createdAt)
        )
    }

    fun toEntity(domain: Reminder): ReminderEntity {
        return ReminderEntity(
            id = domain.id,
            title = domain.title,
            description = domain.description,
            intervalDays = domain.intervalDays,
            nextDueDate = domain.nextDueDate.toEpochDays().toLong(),
            homeItemId = domain.homeItemId,
            category = domain.category.name,
            isActive = domain.isActive,
            lastCompletedDate = domain.lastCompletedDate?.toEpochDays()?.toLong(),
            createdAt = domain.createdAt.toEpochMilliseconds()
        )
    }
}
