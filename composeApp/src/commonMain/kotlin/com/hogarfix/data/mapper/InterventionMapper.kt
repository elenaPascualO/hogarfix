package com.hogarfix.data.mapper

import com.hogarfix.data.local.entity.InterventionEntity
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.DoneBy
import com.hogarfix.domain.model.Intervention
import com.hogarfix.domain.model.Status
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object InterventionMapper {

    private val json = Json { ignoreUnknownKeys = true }

    fun toDomain(entity: InterventionEntity): Intervention {
        return Intervention(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            date = LocalDate.fromEpochDays(entity.date.toInt()),
            category = Category.valueOf(entity.category),
            laborCost = entity.laborCost,
            materialCost = entity.materialCost,
            status = Status.valueOf(entity.status),
            doneBy = DoneBy.valueOf(entity.doneBy),
            professionalId = entity.professionalId,
            homeItemId = entity.homeItemId,
            photoUris = parsePhotoUris(entity.photoUris),
            notes = entity.notes,
            createdAt = Instant.fromEpochMilliseconds(entity.createdAt),
            updatedAt = Instant.fromEpochMilliseconds(entity.updatedAt)
        )
    }

    fun toEntity(domain: Intervention): InterventionEntity {
        return InterventionEntity(
            id = domain.id,
            title = domain.title,
            description = domain.description,
            date = domain.date.toEpochDays().toLong(),
            category = domain.category.name,
            laborCost = domain.laborCost,
            materialCost = domain.materialCost,
            status = domain.status.name,
            doneBy = domain.doneBy.name,
            professionalId = domain.professionalId,
            homeItemId = domain.homeItemId,
            photoUris = json.encodeToString(domain.photoUris),
            notes = domain.notes,
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
