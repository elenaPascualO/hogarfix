package com.hogarfix.data.mapper

import com.hogarfix.data.local.entity.ProfessionalEntity
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Professional
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProfessionalMapperTest {

    private val sampleEntity = ProfessionalEntity(
        id = 1L,
        name = "Juan Perez",
        phone = "+34600123456",
        email = "juan@email.com",
        specialty = "PLUMBING",
        personalRating = 5,
        notes = "Muy profesional",
        createdAt = 1700000000000L
    )

    private val sampleDomain = Professional(
        id = 1L,
        name = "Juan Perez",
        phone = "+34600123456",
        email = "juan@email.com",
        specialty = Category.PLUMBING,
        personalRating = 5,
        notes = "Muy profesional",
        createdAt = Instant.fromEpochMilliseconds(1700000000000L)
    )

    @Test
    fun toDomain_mapsAllFields() {
        val result = ProfessionalMapper.toDomain(sampleEntity)
        assertEquals(sampleDomain, result)
    }

    @Test
    fun toEntity_mapsAllFields() {
        val result = ProfessionalMapper.toEntity(sampleDomain)
        assertEquals(sampleEntity, result)
    }

    @Test
    fun toDomain_handlesNullOptionalFields() {
        val entity = sampleEntity.copy(
            phone = null,
            email = null,
            personalRating = null,
            notes = null
        )
        val result = ProfessionalMapper.toDomain(entity)
        assertNull(result.phone)
        assertNull(result.email)
        assertNull(result.personalRating)
        assertNull(result.notes)
    }

    @Test
    fun roundtrip_preservesAllData() {
        val entity = ProfessionalMapper.toEntity(sampleDomain)
        val domainAgain = ProfessionalMapper.toDomain(entity)
        assertEquals(sampleDomain, domainAgain)
    }

    @Test
    fun toDomain_mapsAllSpecialties() {
        Category.entries.forEach { category ->
            val entity = sampleEntity.copy(specialty = category.name)
            val result = ProfessionalMapper.toDomain(entity)
            assertEquals(category, result.specialty)
        }
    }
}
