package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InterventionUseCaseTest {

    private val repo = FakeInterventionRepository()
    private val getUseCase = GetInterventionsUseCase(repo)
    private val saveUseCase = SaveInterventionUseCase(repo)
    private val deleteUseCase = DeleteInterventionUseCase(repo)

    private fun makeIntervention(
        id: Long = 0L,
        title: String = "Test",
        category: Category = Category.PLUMBING,
        date: LocalDate = LocalDate(2025, 1, 15)
    ) = Intervention(
        id = id,
        title = title,
        date = date,
        category = category,
        doneBy = DoneBy.MYSELF,
        createdAt = Instant.fromEpochMilliseconds(0),
        updatedAt = Instant.fromEpochMilliseconds(0)
    )

    @Test
    fun save_newIntervention_assignsId() = runBlocking {
        val id = saveUseCase(makeIntervention())
        assertNotEquals(0L, id)
    }

    @Test
    fun save_thenGetById_returnsIntervention() = runBlocking {
        val id = saveUseCase(makeIntervention(title = "Fuga"))
        val result = getUseCase.getById(id)
        assertEquals("Fuga", result?.title)
    }

    @Test
    fun save_existingIntervention_updates() = runBlocking {
        val id = saveUseCase(makeIntervention(title = "Original"))
        saveUseCase(makeIntervention(id = id, title = "Updated"))
        val result = getUseCase.getById(id)
        assertEquals("Updated", result?.title)
    }

    @Test
    fun getAll_returnsAllSavedInterventions() = runBlocking {
        saveUseCase(makeIntervention(title = "A"))
        saveUseCase(makeIntervention(title = "B"))
        val all = getUseCase().first()
        assertEquals(2, all.size)
    }

    @Test
    fun getByCategory_filtersCorrectly() = runBlocking {
        saveUseCase(makeIntervention(category = Category.PLUMBING))
        saveUseCase(makeIntervention(category = Category.ELECTRICAL))
        val plumbing = getUseCase.getByCategory(Category.PLUMBING).first()
        assertEquals(1, plumbing.size)
        assertEquals(Category.PLUMBING, plumbing[0].category)
    }

    @Test
    fun delete_removesIntervention() = runBlocking {
        val id = saveUseCase(makeIntervention())
        deleteUseCase(id)
        assertNull(getUseCase.getById(id))
    }

    @Test
    fun delete_nonExistentId_doesNothing() = runBlocking {
        saveUseCase(makeIntervention())
        deleteUseCase(999L)
        val all = getUseCase().first()
        assertEquals(1, all.size)
    }

    @Test
    fun getRecent_returnsLimitedSorted() = runBlocking {
        saveUseCase(makeIntervention(title = "Old", date = LocalDate(2024, 1, 1)))
        saveUseCase(makeIntervention(title = "New", date = LocalDate(2025, 6, 1)))
        saveUseCase(makeIntervention(title = "Mid", date = LocalDate(2025, 3, 1)))
        val recent = getUseCase.getRecent(2).first()
        assertEquals(2, recent.size)
        assertEquals("New", recent[0].title)
        assertEquals("Mid", recent[1].title)
    }

    @Test
    fun getByHomeItem_filtersCorrectly() = runBlocking {
        saveUseCase(makeIntervention(title = "A").copy(homeItemId = 1L))
        saveUseCase(makeIntervention(title = "B").copy(homeItemId = 2L))
        val result = getUseCase.getByHomeItem(1L).first()
        assertEquals(1, result.size)
        assertEquals("A", result[0].title)
    }

    @Test
    fun getByProfessional_filtersCorrectly() = runBlocking {
        saveUseCase(makeIntervention(title = "A").copy(professionalId = 10L))
        saveUseCase(makeIntervention(title = "B").copy(professionalId = 20L))
        val result = getUseCase.getByProfessional(10L).first()
        assertEquals(1, result.size)
        assertEquals("A", result[0].title)
    }

    @Test
    fun getAll_emptyInitially() = runBlocking {
        val all = getUseCase().first()
        assertTrue(all.isEmpty())
    }
}
