package com.hogarfix.domain.usecase

import com.hogarfix.domain.repository.InterventionRepository

class DeleteInterventionUseCase(
    private val repository: InterventionRepository
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
