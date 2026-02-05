package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Intervention
import com.hogarfix.domain.repository.InterventionRepository

class SaveInterventionUseCase(
    private val repository: InterventionRepository
) {
    suspend operator fun invoke(intervention: Intervention): Long =
        repository.save(intervention)

    suspend fun savePhoto(interventionId: Long, photoBytes: ByteArray): String =
        repository.savePhoto(interventionId, photoBytes)

    suspend fun deletePhoto(photoUri: String) =
        repository.deletePhoto(photoUri)
}
