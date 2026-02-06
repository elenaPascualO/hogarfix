package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.HomeItem
import com.hogarfix.domain.repository.HomeItemRepository
import kotlinx.coroutines.flow.Flow

class GetHomeItemsUseCase(
    private val repository: HomeItemRepository
) {
    operator fun invoke(): Flow<List<HomeItem>> = repository.getAll()

    fun getByCategory(category: Category): Flow<List<HomeItem>> =
        repository.getByCategory(category)

    fun getWithExpiringWarranty(daysAhead: Int = 90): Flow<List<HomeItem>> =
        repository.getWithExpiringWarranty(daysAhead)

    suspend fun getById(id: Long): HomeItem? = repository.getById(id)
}
