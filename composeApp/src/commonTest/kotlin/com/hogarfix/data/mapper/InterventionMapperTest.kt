package com.hogarfix.data.mapper

import com.hogarfix.data.local.entity.InterventionEntity
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.DoneBy
import com.hogarfix.domain.model.Intervention
import com.hogarfix.domain.model.Status
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InterventionMapperTest {

    private val sampleEntity = InterventionEntity(
        id = 1L,
        title = "Arreglo fuga",
        description = "Fuga en cocina",
        date = 20088L, // 2024-12-19
        category = "PLUMBING",
        laborCost = 80.0,
        materialCost = 40.0,
        status = "COMPLETED",
        doneBy = "PROFESSIONAL",
        professionalId = 5L,
        homeItemId = 3L,
        photoUris = """["photo1.jpg","photo2.jpg"]""",
        notes = "Cambiar junta",
        createdAt = 1700000000000L,
        updatedAt = 1700001000000L
    )

    private val sampleDomain = Intervention(
        id = 1L,
        title = "Arreglo fuga",
        description = "Fuga en cocina",
        date = LocalDate.fromEpochDays(20088),
        category = Category.PLUMBING,
        laborCost = 80.0,
        materialCost = 40.0,
        status = Status.COMPLETED,
        doneBy = DoneBy.PROFESSIONAL,
        professionalId = 5L,
        homeItemId = 3L,
        photoUris = listOf("photo1.jpg", "photo2.jpg"),
        notes = "Cambiar junta",
        createdAt = Instant.fromEpochMilliseconds(1700000000000L),
        updatedAt = Instant.fromEpochMilliseconds(1700001000000L)
    )

    @Test
    fun toDomain_mapsAllFields() {
        val result = InterventionMapper.toDomain(sampleEntity)
        assertEquals(sampleDomain, result)
    }

    @Test
    fun toEntity_mapsAllFields() {
        val result = InterventionMapper.toEntity(sampleDomain)
        assertEquals(sampleEntity.id, result.id)
        assertEquals(sampleEntity.title, result.title)
        assertEquals(sampleEntity.description, result.description)
        assertEquals(sampleEntity.date, result.date)
        assertEquals(sampleEntity.category, result.category)
        assertEquals(sampleEntity.laborCost, result.laborCost)
        assertEquals(sampleEntity.materialCost, result.materialCost)
        assertEquals(sampleEntity.status, result.status)
        assertEquals(sampleEntity.doneBy, result.doneBy)
        assertEquals(sampleEntity.professionalId, result.professionalId)
        assertEquals(sampleEntity.homeItemId, result.homeItemId)
        assertEquals(sampleEntity.notes, result.notes)
        assertEquals(sampleEntity.createdAt, result.createdAt)
        assertEquals(sampleEntity.updatedAt, result.updatedAt)
    }

    @Test
    fun toDomain_handlesNullOptionalFields() {
        val entity = sampleEntity.copy(
            description = null,
            laborCost = null,
            materialCost = null,
            professionalId = null,
            homeItemId = null,
            notes = null
        )
        val result = InterventionMapper.toDomain(entity)
        assertNull(result.description)
        assertNull(result.laborCost)
        assertNull(result.materialCost)
        assertNull(result.professionalId)
        assertNull(result.homeItemId)
        assertNull(result.notes)
    }

    @Test
    fun toDomain_handlesEmptyPhotoUris() {
        val entity = sampleEntity.copy(photoUris = "[]")
        val result = InterventionMapper.toDomain(entity)
        assertEquals(emptyList(), result.photoUris)
    }

    @Test
    fun toDomain_handlesInvalidPhotoUrisJson() {
        val entity = sampleEntity.copy(photoUris = "invalid json")
        val result = InterventionMapper.toDomain(entity)
        assertEquals(emptyList(), result.photoUris)
    }

    @Test
    fun roundtrip_preservesAllData() {
        val entity = InterventionMapper.toEntity(sampleDomain)
        val domainAgain = InterventionMapper.toDomain(entity)
        assertEquals(sampleDomain, domainAgain)
    }

    @Test
    fun toDomain_mapsAllCategories() {
        Category.entries.forEach { category ->
            val entity = sampleEntity.copy(category = category.name)
            val result = InterventionMapper.toDomain(entity)
            assertEquals(category, result.category)
        }
    }

    @Test
    fun toDomain_mapsAllStatuses() {
        Status.entries.forEach { status ->
            val entity = sampleEntity.copy(status = status.name)
            val result = InterventionMapper.toDomain(entity)
            assertEquals(status, result.status)
        }
    }

    @Test
    fun toDomain_mapsAllDoneByValues() {
        DoneBy.entries.forEach { doneBy ->
            val entity = sampleEntity.copy(doneBy = doneBy.name)
            val result = InterventionMapper.toDomain(entity)
            assertEquals(doneBy, result.doneBy)
        }
    }
}
