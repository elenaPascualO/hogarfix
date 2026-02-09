package com.hogarfix.domain.usecase

import com.hogarfix.domain.repository.ProfessionalRepository

class DeleteProfessionalUseCase(
    private val repository: ProfessionalRepository
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
