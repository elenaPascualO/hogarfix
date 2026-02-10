package com.hogarfix.domain.repository

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Intervention
import kotlinx.coroutines.flow.Flow

interface InterventionRepository {
    fun getAll(): Flow<List<Intervention>>
    fun getRecent(limit: Int): Flow<List<Intervention>>
    fun getByCategory(category: Category): Flow<List<Intervention>>
    fun getByHomeItem(itemId: Long): Flow<List<Intervention>>
    fun getByProfessional(professionalId: Long): Flow<List<Intervention>>
    fun searchByText(query: String): Flow<List<Intervention>>
    suspend fun getById(id: Long): Intervention?
    suspend fun save(intervention: Intervention): Long
    suspend fun delete(id: Long)
    suspend fun savePhoto(interventionId: Long, photoBytes: ByteArray): String
    suspend fun deletePhoto(photoUri: String)
}
