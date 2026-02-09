package com.hogarfix.data.mapper

import com.hogarfix.data.local.entity.ReminderEntity
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Reminder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ReminderMapperTest {

    private val sampleEntity = ReminderEntity(
        id = 1L,
        title = "Revision caldera",
        description = "Revision anual",
        intervalDays = 365,
        nextDueDate = 20088L,
        homeItemId = 3L,
        category = "HVAC",
        isActive = true,
        lastCompletedDate = 19723L,
        createdAt = 1700000000000L
    )

    private val sampleDomain = Reminder(
        id = 1L,
        title = "Revision caldera",
        description = "Revision anual",
        intervalDays = 365,
        nextDueDate = LocalDate.fromEpochDays(20088),
        homeItemId = 3L,
        category = Category.HVAC,
        isActive = true,
        lastCompletedDate = LocalDate.fromEpochDays(19723),
        createdAt = Instant.fromEpochMilliseconds(1700000000000L)
    )

    @Test
    fun toDomain_mapsAllFields() {
        val result = ReminderMapper.toDomain(sampleEntity)
        assertEquals(sampleDomain, result)
    }

    @Test
    fun toEntity_mapsAllFields() {
        val result = ReminderMapper.toEntity(sampleDomain)
        assertEquals(sampleEntity, result)
    }

    @Test
    fun toDomain_handlesNullOptionalFields() {
        val entity = sampleEntity.copy(
            description = null,
            homeItemId = null,
            lastCompletedDate = null
        )
        val result = ReminderMapper.toDomain(entity)
        assertNull(result.description)
        assertNull(result.homeItemId)
        assertNull(result.lastCompletedDate)
    }

    @Test
    fun toEntity_handlesNullOptionalFields() {
        val domain = sampleDomain.copy(
            description = null,
            homeItemId = null,
            lastCompletedDate = null
        )
        val result = ReminderMapper.toEntity(domain)
        assertNull(result.description)
        assertNull(result.homeItemId)
        assertNull(result.lastCompletedDate)
    }

    @Test
    fun roundtrip_preservesAllData() {
        val entity = ReminderMapper.toEntity(sampleDomain)
        val domainAgain = ReminderMapper.toDomain(entity)
        assertEquals(sampleDomain, domainAgain)
    }

    @Test
    fun toDomain_mapsInactiveReminder() {
        val entity = sampleEntity.copy(isActive = false)
        val result = ReminderMapper.toDomain(entity)
        assertEquals(false, result.isActive)
    }

    @Test
    fun toDomain_mapsAllCategories() {
        Category.entries.forEach { category ->
            val entity = sampleEntity.copy(category = category.name)
            val result = ReminderMapper.toDomain(entity)
            assertEquals(category, result.category)
        }
    }
}
