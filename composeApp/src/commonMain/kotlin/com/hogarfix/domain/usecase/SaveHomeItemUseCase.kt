package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.HomeItem
import com.hogarfix.domain.repository.HomeItemRepository

class SaveHomeItemUseCase(
    private val repository: HomeItemRepository
) {
    suspend operator fun invoke(homeItem: HomeItem): Long =
        repository.save(homeItem)

    suspend fun savePhoto(homeItemId: Long, photoBytes: ByteArray): String =
        repository.savePhoto(homeItemId, photoBytes)

    suspend fun deletePhoto(photoUri: String) =
        repository.deletePhoto(photoUri)
}
