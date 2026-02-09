package com.hogarfix.data.mapper

import com.hogarfix.data.local.entity.ProfessionalEntity
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Professional
import kotlinx.datetime.Instant

object ProfessionalMapper {

    fun toDomain(entity: ProfessionalEntity): Professional {
        return Professional(
            id = entity.id,
            name = entity.name,
            phone = entity.phone,
            email = entity.email,
            specialty = Category.valueOf(entity.specialty),
            personalRating = entity.personalRating,
            notes = entity.notes,
            createdAt = Instant.fromEpochMilliseconds(entity.createdAt)
        )
    }

    fun toEntity(domain: Professional): ProfessionalEntity {
        return ProfessionalEntity(
            id = domain.id,
            name = domain.name,
            phone = domain.phone,
            email = domain.email,
            specialty = domain.specialty.name,
            personalRating = domain.personalRating,
            notes = domain.notes,
            createdAt = domain.createdAt.toEpochMilliseconds()
        )
    }
}
