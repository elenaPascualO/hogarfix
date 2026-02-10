package com.hogarfix.domain.repository

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.HomeItem
import kotlinx.coroutines.flow.Flow

interface HomeItemRepository {
    fun getAll(): Flow<List<HomeItem>>
    fun getByCategory(category: Category): Flow<List<HomeItem>>
    fun getWithExpiringWarranty(daysAhead: Int): Flow<List<HomeItem>>
    fun searchByText(query: String): Flow<List<HomeItem>>
    suspend fun getById(id: Long): HomeItem?
    suspend fun save(homeItem: HomeItem): Long
    suspend fun delete(id: Long)
    suspend fun savePhoto(homeItemId: Long, photoBytes: ByteArray): String
    suspend fun deletePhoto(photoUri: String)
}
