package com.hogarfix.data.repository

import com.hogarfix.data.local.dao.InterventionDao
import com.hogarfix.data.mapper.InterventionMapper
import com.hogarfix.data.storage.PhotoStorage
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Intervention
import com.hogarfix.domain.repository.InterventionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InterventionRepositoryImpl(
    private val interventionDao: InterventionDao,
    private val photoStorage: PhotoStorage
) : InterventionRepository {

    override fun getAll(): Flow<List<Intervention>> {
        return interventionDao.getAll().map { entities ->
            entities.map { InterventionMapper.toDomain(it) }
        }
    }

    override fun getRecent(limit: Int): Flow<List<Intervention>> {
        return interventionDao.getRecent(limit).map { entities ->
            entities.map { InterventionMapper.toDomain(it) }
        }
    }

    override fun getByCategory(category: Category): Flow<List<Intervention>> {
        return interventionDao.getByCategory(category.name).map { entities ->
            entities.map { InterventionMapper.toDomain(it) }
        }
    }

    override fun getByHomeItem(itemId: Long): Flow<List<Intervention>> {
        return interventionDao.getByHomeItem(itemId).map { entities ->
            entities.map { InterventionMapper.toDomain(it) }
        }
    }

    override fun getByProfessional(professionalId: Long): Flow<List<Intervention>> {
        return interventionDao.getByProfessional(professionalId).map { entities ->
            entities.map { InterventionMapper.toDomain(it) }
        }
    }

    override suspend fun getById(id: Long): Intervention? {
        return interventionDao.getById(id)?.let { InterventionMapper.toDomain(it) }
    }

    override suspend fun save(intervention: Intervention): Long {
        val entity = InterventionMapper.toEntity(intervention)
        return if (intervention.id == 0L) {
            interventionDao.insert(entity)
        } else {
            interventionDao.update(entity)
            intervention.id
        }
    }

    override suspend fun delete(id: Long) {
        // Get intervention to delete associated photos
        val intervention = interventionDao.getById(id)
        intervention?.let {
            val photoUris = InterventionMapper.toDomain(it).photoUris
            photoUris.forEach { uri -> photoStorage.deletePhoto(uri) }
        }
        interventionDao.deleteById(id)
    }

    override suspend fun savePhoto(interventionId: Long, photoBytes: ByteArray): String {
        return photoStorage.savePhoto("intervention", interventionId, photoBytes)
    }

    override suspend fun deletePhoto(photoUri: String) {
        photoStorage.deletePhoto(photoUri)
    }
}
