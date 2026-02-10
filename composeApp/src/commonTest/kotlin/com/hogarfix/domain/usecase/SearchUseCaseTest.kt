package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.DoneBy
import com.hogarfix.domain.model.HomeItem
import com.hogarfix.domain.model.Intervention
import com.hogarfix.domain.model.Professional
import com.hogarfix.domain.model.Reminder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchUseCaseTest {

    private lateinit var interventionRepo: FakeInterventionRepository
    private lateinit var homeItemRepo: FakeHomeItemRepository
    private lateinit var professionalRepo: FakeProfessionalRepository
    private lateinit var reminderRepo: FakeReminderRepository
    private lateinit var searchUseCase: SearchUseCase

    @BeforeTest
    fun setup() {
        interventionRepo = FakeInterventionRepository()
        homeItemRepo = FakeHomeItemRepository()
        professionalRepo = FakeProfessionalRepository()
        reminderRepo = FakeReminderRepository()
        searchUseCase = SearchUseCase(
            interventionRepo, homeItemRepo, professionalRepo, reminderRepo
        )
    }

    @Test
    fun search_emptyRepos_returnsEmpty() = runBlocking {
        val results = searchUseCase("test").first()
        assertTrue(results.isEmpty)
    }

    @Test
    fun search_findsInterventionByTitle() = runBlocking {
        interventionRepo.save(
            Intervention(
                title = "Arreglo grifo cocina",
                date = LocalDate(2025, 1, 1),
                category = Category.PLUMBING,
                doneBy = DoneBy.MYSELF,
                createdAt = Instant.fromEpochMilliseconds(0),
                updatedAt = Instant.fromEpochMilliseconds(0)
            )
        )
        val results = searchUseCase("grifo").first()
        assertEquals(1, results.interventions.size)
        assertEquals("Arreglo grifo cocina", results.interventions.first().title)
    }

    @Test
    fun search_findsHomeItemByName() = runBlocking {
        homeItemRepo.save(
            HomeItem(
                name = "Lavadora Samsung",
                category = Category.APPLIANCES,
                brand = "Samsung",
                createdAt = Instant.fromEpochMilliseconds(0),
                updatedAt = Instant.fromEpochMilliseconds(0)
            )
        )
        val results = searchUseCase("Lavadora").first()
        assertEquals(1, results.homeItems.size)
        assertEquals("Lavadora Samsung", results.homeItems.first().title)
    }

    @Test
    fun search_findsHomeItemByBrand() = runBlocking {
        homeItemRepo.save(
            HomeItem(
                name = "Lavadora",
                brand = "Samsung",
                category = Category.APPLIANCES,
                createdAt = Instant.fromEpochMilliseconds(0),
                updatedAt = Instant.fromEpochMilliseconds(0)
            )
        )
        val results = searchUseCase("Samsung").first()
        assertEquals(1, results.homeItems.size)
    }

    @Test
    fun search_findsProfessionalByName() = runBlocking {
        professionalRepo.save(
            Professional(
                name = "Juan Fontanero",
                specialty = Category.PLUMBING,
                createdAt = Instant.fromEpochMilliseconds(0)
            )
        )
        val results = searchUseCase("Juan").first()
        assertEquals(1, results.professionals.size)
        assertEquals("Juan Fontanero", results.professionals.first().title)
    }

    @Test
    fun search_findsReminderByTitle() = runBlocking {
        reminderRepo.save(
            Reminder(
                title = "Revision caldera",
                intervalDays = 365,
                nextDueDate = LocalDate(2025, 6, 1),
                category = Category.HVAC,
                createdAt = Instant.fromEpochMilliseconds(0)
            )
        )
        val results = searchUseCase("caldera").first()
        assertEquals(1, results.reminders.size)
        assertEquals("Revision caldera", results.reminders.first().title)
    }

    @Test
    fun search_findsAcrossMultipleModules() = runBlocking {
        interventionRepo.save(
            Intervention(
                title = "Limpieza filtros",
                date = LocalDate(2025, 1, 1),
                category = Category.HVAC,
                doneBy = DoneBy.MYSELF,
                createdAt = Instant.fromEpochMilliseconds(0),
                updatedAt = Instant.fromEpochMilliseconds(0)
            )
        )
        reminderRepo.save(
            Reminder(
                title = "Limpieza filtros AC",
                intervalDays = 180,
                nextDueDate = LocalDate(2025, 6, 1),
                category = Category.HVAC,
                createdAt = Instant.fromEpochMilliseconds(0)
            )
        )
        val results = searchUseCase("filtros").first()
        assertEquals(1, results.interventions.size)
        assertEquals(1, results.reminders.size)
        assertEquals(2, results.totalCount)
    }

    @Test
    fun search_caseInsensitive() = runBlocking {
        interventionRepo.save(
            Intervention(
                title = "Arreglo GRIFO",
                date = LocalDate(2025, 1, 1),
                category = Category.PLUMBING,
                doneBy = DoneBy.MYSELF,
                createdAt = Instant.fromEpochMilliseconds(0),
                updatedAt = Instant.fromEpochMilliseconds(0)
            )
        )
        val results = searchUseCase("grifo").first()
        assertEquals(1, results.interventions.size)
    }

    @Test
    fun search_noMatch_returnsEmpty() = runBlocking {
        interventionRepo.save(
            Intervention(
                title = "Arreglo grifo",
                date = LocalDate(2025, 1, 1),
                category = Category.PLUMBING,
                doneBy = DoneBy.MYSELF,
                createdAt = Instant.fromEpochMilliseconds(0),
                updatedAt = Instant.fromEpochMilliseconds(0)
            )
        )
        val results = searchUseCase("electricidad").first()
        assertTrue(results.isEmpty)
    }
}
