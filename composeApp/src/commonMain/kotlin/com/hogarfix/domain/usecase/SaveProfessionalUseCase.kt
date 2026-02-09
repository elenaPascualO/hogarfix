package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Professional
import com.hogarfix.domain.repository.ProfessionalRepository

class SaveProfessionalUseCase(
    private val repository: ProfessionalRepository
) {
    suspend operator fun invoke(professional: Professional): Long =
        repository.save(professional)
}
