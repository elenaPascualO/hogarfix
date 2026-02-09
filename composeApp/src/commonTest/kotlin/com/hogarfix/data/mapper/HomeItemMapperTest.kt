package com.hogarfix.data.mapper

import com.hogarfix.data.local.entity.HomeItemEntity
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.HomeItem
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HomeItemMapperTest {

    private val sampleEntity = HomeItemEntity(
        id = 1L,
        name = "Lavadora",
        brand = "Samsung",
        model = "WW90",
        category = "APPLIANCES",
        purchaseDate = 19723L,
        warrantyEndDate = 20088L,
        location = "Cocina",
        notes = "Programa eco",
        photoUris = """["foto1.jpg"]""",
        createdAt = 1700000000000L,
        updatedAt = 1700001000000L
    )

    private val sampleDomain = HomeItem(
        id = 1L,
        name = "Lavadora",
        brand = "Samsung",
        model = "WW90",
        category = Category.APPLIANCES,
        purchaseDate = LocalDate.fromEpochDays(19723),
        warrantyEndDate = LocalDate.fromEpochDays(20088),
        location = "Cocina",
        notes = "Programa eco",
        photoUris = listOf("foto1.jpg"),
        createdAt = Instant.fromEpochMilliseconds(1700000000000L),
        updatedAt = Instant.fromEpochMilliseconds(1700001000000L)
    )

    @Test
    fun toDomain_mapsAllFields() {
        val result = HomeItemMapper.toDomain(sampleEntity)
        assertEquals(sampleDomain, result)
    }

    @Test
    fun toEntity_mapsAllFields() {
        val result = HomeItemMapper.toEntity(sampleDomain)
        assertEquals(sampleEntity.id, result.id)
        assertEquals(sampleEntity.name, result.name)
        assertEquals(sampleEntity.brand, result.brand)
        assertEquals(sampleEntity.model, result.model)
        assertEquals(sampleEntity.category, result.category)
        assertEquals(sampleEntity.purchaseDate, result.purchaseDate)
        assertEquals(sampleEntity.warrantyEndDate, result.warrantyEndDate)
        assertEquals(sampleEntity.location, result.location)
        assertEquals(sampleEntity.notes, result.notes)
        assertEquals(sampleEntity.createdAt, result.createdAt)
        assertEquals(sampleEntity.updatedAt, result.updatedAt)
    }

    @Test
    fun toDomain_handlesNullOptionalFields() {
        val entity = sampleEntity.copy(
            brand = null,
            model = null,
            purchaseDate = null,
            warrantyEndDate = null,
            location = null,
            notes = null
        )
        val result = HomeItemMapper.toDomain(entity)
        assertNull(result.brand)
        assertNull(result.model)
        assertNull(result.purchaseDate)
        assertNull(result.warrantyEndDate)
        assertNull(result.location)
        assertNull(result.notes)
    }

    @Test
    fun toDomain_handlesEmptyPhotoUris() {
        val entity = sampleEntity.copy(photoUris = "[]")
        val result = HomeItemMapper.toDomain(entity)
        assertEquals(emptyList(), result.photoUris)
    }

    @Test
    fun toDomain_handlesInvalidPhotoUrisJson() {
        val entity = sampleEntity.copy(photoUris = "not json")
        val result = HomeItemMapper.toDomain(entity)
        assertEquals(emptyList(), result.photoUris)
    }

    @Test
    fun roundtrip_preservesAllData() {
        val entity = HomeItemMapper.toEntity(sampleDomain)
        val domainAgain = HomeItemMapper.toDomain(entity)
        assertEquals(sampleDomain, domainAgain)
    }

    @Test
    fun toDomain_mapsAllCategories() {
        Category.entries.forEach { category ->
            val entity = sampleEntity.copy(category = category.name)
            val result = HomeItemMapper.toDomain(entity)
            assertEquals(category, result.category)
        }
    }
}
