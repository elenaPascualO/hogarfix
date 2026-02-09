package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Professional
import com.hogarfix.domain.repository.ProfessionalRepository
import kotlinx.coroutines.flow.Flow

class GetProfessionalsUseCase(
    private val repository: ProfessionalRepository
) {
    operator fun invoke(): Flow<List<Professional>> = repository.getAll()

    fun getBySpecialty(category: Category): Flow<List<Professional>> =
        repository.getBySpecialty(category)

    suspend fun getById(id: Long): Professional? = repository.getById(id)
}
