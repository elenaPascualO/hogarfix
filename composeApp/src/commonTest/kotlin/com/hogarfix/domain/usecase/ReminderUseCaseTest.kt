package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Reminder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReminderUseCaseTest {

    private val repo = FakeReminderRepository()
    private val getUseCase = GetRemindersUseCase(repo)
    private val saveUseCase = SaveReminderUseCase(repo)
    private val deleteUseCase = DeleteReminderUseCase(repo)
    private val completeUseCase = CompleteReminderUseCase(repo)

    private fun makeReminder(
        id: Long = 0L,
        title: String = "Revision caldera",
        category: Category = Category.HVAC,
        intervalDays: Int = 365,
        isActive: Boolean = true
    ) = Reminder(
        id = id,
        title = title,
        intervalDays = intervalDays,
        nextDueDate = LocalDate(2025, 6, 1),
        category = category,
        isActive = isActive,
        createdAt = Instant.fromEpochMilliseconds(0)
    )

    @Test
    fun save_newReminder_assignsId() = runBlocking {
        val id = saveUseCase(makeReminder())
        assertNotEquals(0L, id)
    }

    @Test
    fun save_thenGetById_returnsReminder() = runBlocking {
        val id = saveUseCase(makeReminder(title = "Filtros AC"))
        val result = getUseCase.getById(id)
        assertEquals("Filtros AC", result?.title)
    }

    @Test
    fun save_existingReminder_updates() = runBlocking {
        val id = saveUseCase(makeReminder(title = "Original"))
        saveUseCase(makeReminder(id = id, title = "Updated"))
        val result = getUseCase.getById(id)
        assertEquals("Updated", result?.title)
    }

    @Test
    fun save_updateIntervalDays() = runBlocking {
        val id = saveUseCase(makeReminder(intervalDays = 30))
        saveUseCase(makeReminder(id = id, intervalDays = 1))
        val result = getUseCase.getById(id)
        assertEquals(1, result?.intervalDays)
    }

    @Test
    fun getAllActive_returnsOnlyActive() = runBlocking {
        saveUseCase(makeReminder(title = "Active", isActive = true))
        saveUseCase(makeReminder(title = "Inactive", isActive = false))
        val active = getUseCase().first()
        assertEquals(1, active.size)
        assertEquals("Active", active[0].title)
    }

    @Test
    fun delete_removesReminder() = runBlocking {
        val id = saveUseCase(makeReminder())
        deleteUseCase(id)
        assertNull(getUseCase.getById(id))
    }

    @Test
    fun delete_nonExistentId_doesNothing() = runBlocking {
        saveUseCase(makeReminder())
        deleteUseCase(999L)
        val all = getUseCase().first()
        assertEquals(1, all.size)
    }

    @Test
    fun complete_delegatesToRepository() = runBlocking {
        val id = saveUseCase(makeReminder())
        completeUseCase(id)
        assertEquals(id, repo.lastCompletedId)
    }

    @Test
    fun complete_updatesNextDueDate() = runBlocking {
        val id = saveUseCase(makeReminder(intervalDays = 30))
        completeUseCase(id)
        val result = getUseCase.getById(id)
        assertNotNull(result)
        assertNotNull(result.lastCompletedDate)
        // nextDueDate should be 30 days after "today" (2025-01-15 in fake)
        assertEquals(LocalDate(2025, 2, 14), result.nextDueDate)
    }

    @Test
    fun getByHomeItem_filtersCorrectly() = runBlocking {
        saveUseCase(makeReminder(title = "A").copy(homeItemId = 1L))
        saveUseCase(makeReminder(title = "B").copy(homeItemId = 2L))
        val result = getUseCase.getByHomeItem(1L).first()
        assertEquals(1, result.size)
        assertEquals("A", result[0].title)
    }

    @Test
    fun getAll_emptyInitially() = runBlocking {
        val all = getUseCase().first()
        assertTrue(all.isEmpty())
    }
}
