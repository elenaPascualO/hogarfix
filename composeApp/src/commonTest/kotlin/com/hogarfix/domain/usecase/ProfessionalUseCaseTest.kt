package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Professional
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProfessionalUseCaseTest {

    private val repo = FakeProfessionalRepository()
    private val getUseCase = GetProfessionalsUseCase(repo)
    private val saveUseCase = SaveProfessionalUseCase(repo)
    private val deleteUseCase = DeleteProfessionalUseCase(repo)

    private fun makeProfessional(
        id: Long = 0L,
        name: String = "Juan",
        specialty: Category = Category.PLUMBING
    ) = Professional(
        id = id,
        name = name,
        specialty = specialty,
        createdAt = Instant.fromEpochMilliseconds(0)
    )

    @Test
    fun save_newProfessional_assignsId() = runBlocking {
        val id = saveUseCase(makeProfessional())
        assertNotEquals(0L, id)
    }

    @Test
    fun save_thenGetById_returnsProfessional() = runBlocking {
        val id = saveUseCase(makeProfessional(name = "Maria"))
        val result = getUseCase.getById(id)
        assertEquals("Maria", result?.name)
    }

    @Test
    fun save_existingProfessional_updates() = runBlocking {
        val id = saveUseCase(makeProfessional(name = "Original"))
        saveUseCase(makeProfessional(id = id, name = "Updated"))
        val result = getUseCase.getById(id)
        assertEquals("Updated", result?.name)
    }

    @Test
    fun getAll_returnsAllProfessionals() = runBlocking {
        saveUseCase(makeProfessional(name = "A"))
        saveUseCase(makeProfessional(name = "B"))
        val all = getUseCase().first()
        assertEquals(2, all.size)
    }

    @Test
    fun getBySpecialty_filtersCorrectly() = runBlocking {
        saveUseCase(makeProfessional(specialty = Category.PLUMBING))
        saveUseCase(makeProfessional(specialty = Category.ELECTRICAL))
        val plumbers = getUseCase.getBySpecialty(Category.PLUMBING).first()
        assertEquals(1, plumbers.size)
        assertEquals(Category.PLUMBING, plumbers[0].specialty)
    }

    @Test
    fun delete_removesProfessional() = runBlocking {
        val id = saveUseCase(makeProfessional())
        deleteUseCase(id)
        assertNull(getUseCase.getById(id))
    }

    @Test
    fun delete_nonExistentId_doesNothing() = runBlocking {
        saveUseCase(makeProfessional())
        deleteUseCase(999L)
        val all = getUseCase().first()
        assertEquals(1, all.size)
    }

    @Test
    fun getAll_emptyInitially() = runBlocking {
        val all = getUseCase().first()
        assertTrue(all.isEmpty())
    }
}
