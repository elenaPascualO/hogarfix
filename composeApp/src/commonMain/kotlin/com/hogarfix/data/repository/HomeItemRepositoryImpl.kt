package com.hogarfix.data.repository

import com.hogarfix.data.local.dao.HomeItemDao
import com.hogarfix.data.mapper.HomeItemMapper
import com.hogarfix.data.storage.PhotoStorage
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.HomeItem
import com.hogarfix.domain.repository.HomeItemRepository
import com.hogarfix.util.currentDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

class HomeItemRepositoryImpl(
    private val homeItemDao: HomeItemDao,
    private val photoStorage: PhotoStorage
) : HomeItemRepository {

    override fun getAll(): Flow<List<HomeItem>> {
        return homeItemDao.getAll().map { entities ->
            entities.map { HomeItemMapper.toDomain(it) }
        }
    }

    override fun getByCategory(category: Category): Flow<List<HomeItem>> {
        return homeItemDao.getByCategory(category.name).map { entities ->
            entities.map { HomeItemMapper.toDomain(it) }
        }
    }

    override fun getWithExpiringWarranty(daysAhead: Int): Flow<List<HomeItem>> {
        val today = currentDate()
        val futureDate = today.plus(daysAhead, DateTimeUnit.DAY)
        return homeItemDao.getWithExpiringWarranty(
            today = today.toEpochDays().toLong(),
            futureDate = futureDate.toEpochDays().toLong()
        ).map { entities ->
            entities.map { HomeItemMapper.toDomain(it) }
        }
    }

    override suspend fun getById(id: Long): HomeItem? {
        return homeItemDao.getById(id)?.let { HomeItemMapper.toDomain(it) }
    }

    override suspend fun save(homeItem: HomeItem): Long {
        val entity = HomeItemMapper.toEntity(homeItem)
        return if (homeItem.id == 0L) {
            homeItemDao.insert(entity)
        } else {
            homeItemDao.update(entity)
            homeItem.id
        }
    }

    override suspend fun delete(id: Long) {
        val homeItem = homeItemDao.getById(id)
        homeItem?.let {
            val photoUris = HomeItemMapper.toDomain(it).photoUris
            photoUris.forEach { uri -> photoStorage.deletePhoto(uri) }
        }
        homeItemDao.deleteById(id)
    }

    override suspend fun savePhoto(homeItemId: Long, photoBytes: ByteArray): String {
        return photoStorage.savePhoto("homeitem", homeItemId, photoBytes)
    }

    override suspend fun deletePhoto(photoUri: String) {
        photoStorage.deletePhoto(photoUri)
    }
}
