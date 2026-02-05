package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Intervention
import com.hogarfix.domain.repository.InterventionRepository
import kotlinx.coroutines.flow.Flow

class GetInterventionsUseCase(
    private val repository: InterventionRepository
) {
    operator fun invoke(): Flow<List<Intervention>> = repository.getAll()

    fun getRecent(limit: Int): Flow<List<Intervention>> = repository.getRecent(limit)

    fun getByCategory(category: Category): Flow<List<Intervention>> =
        repository.getByCategory(category)

    fun getByHomeItem(itemId: Long): Flow<List<Intervention>> =
        repository.getByHomeItem(itemId)

    fun getByProfessional(professionalId: Long): Flow<List<Intervention>> =
        repository.getByProfessional(professionalId)

    suspend fun getById(id: Long): Intervention? = repository.getById(id)
}
