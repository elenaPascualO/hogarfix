package com.hogarfix.domain.usecase

import com.hogarfix.domain.repository.HomeItemRepository

class DeleteHomeItemUseCase(
    private val repository: HomeItemRepository
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
