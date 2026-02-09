package com.hogarfix.data.repository

import com.hogarfix.data.local.dao.ProfessionalDao
import com.hogarfix.data.mapper.ProfessionalMapper
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Professional
import com.hogarfix.domain.repository.ProfessionalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfessionalRepositoryImpl(
    private val professionalDao: ProfessionalDao
) : ProfessionalRepository {

    override fun getAll(): Flow<List<Professional>> {
        return professionalDao.getAll().map { entities ->
            entities.map { ProfessionalMapper.toDomain(it) }
        }
    }

    override fun getBySpecialty(category: Category): Flow<List<Professional>> {
        return professionalDao.getBySpecialty(category.name).map { entities ->
            entities.map { ProfessionalMapper.toDomain(it) }
        }
    }

    override suspend fun getById(id: Long): Professional? {
        return professionalDao.getById(id)?.let { ProfessionalMapper.toDomain(it) }
    }

    override suspend fun save(professional: Professional): Long {
        val entity = ProfessionalMapper.toEntity(professional)
        return if (professional.id == 0L) {
            professionalDao.insert(entity)
        } else {
            professionalDao.update(entity)
            professional.id
        }
    }

    override suspend fun delete(id: Long) {
        professionalDao.deleteById(id)
    }
}
