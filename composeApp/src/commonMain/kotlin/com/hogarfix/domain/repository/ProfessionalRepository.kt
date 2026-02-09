package com.hogarfix.domain.repository

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Professional
import kotlinx.coroutines.flow.Flow

interface ProfessionalRepository {
    fun getAll(): Flow<List<Professional>>
    fun getBySpecialty(category: Category): Flow<List<Professional>>
    suspend fun getById(id: Long): Professional?
    suspend fun save(professional: Professional): Long
    suspend fun delete(id: Long)
}
