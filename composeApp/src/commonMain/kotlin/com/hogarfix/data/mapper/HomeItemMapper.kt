package com.hogarfix.data.mapper

import com.hogarfix.data.local.entity.HomeItemEntity
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.HomeItem
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object HomeItemMapper {

    private val json = Json { ignoreUnknownKeys = true }

    fun toDomain(entity: HomeItemEntity): HomeItem {
        return HomeItem(
            id = entity.id,
            name = entity.name,
            brand = entity.brand,
            model = entity.model,
            category = Category.valueOf(entity.category),
            purchaseDate = entity.purchaseDate?.let { LocalDate.fromEpochDays(it.toInt()) },
            warrantyEndDate = entity.warrantyEndDate?.let { LocalDate.fromEpochDays(it.toInt()) },
            location = entity.location,
            notes = entity.notes,
            photoUris = parsePhotoUris(entity.photoUris),
            createdAt = Instant.fromEpochMilliseconds(entity.createdAt),
            updatedAt = Instant.fromEpochMilliseconds(entity.updatedAt)
        )
    }

    fun toEntity(domain: HomeItem): HomeItemEntity {
        return HomeItemEntity(
            id = domain.id,
            name = domain.name,
            brand = domain.brand,
            model = domain.model,
            category = domain.category.name,
            purchaseDate = domain.purchaseDate?.toEpochDays()?.toLong(),
            warrantyEndDate = domain.warrantyEndDate?.toEpochDays()?.toLong(),
            location = domain.location,
            notes = domain.notes,
            photoUris = json.encodeToString(domain.photoUris),
            createdAt = domain.createdAt.toEpochMilliseconds(),
            updatedAt = domain.updatedAt.toEpochMilliseconds()
        )
    }

    private fun parsePhotoUris(jsonString: String): List<String> {
        return try {
            json.decodeFromString<List<String>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
