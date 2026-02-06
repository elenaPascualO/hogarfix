package com.hogarfix.ui.screens.inventory

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.HomeItem

data class HomeItemListState(
    val homeItems: List<HomeItem> = emptyList(),
    val interventionCounts: Map<Long, Int> = emptyMap(),
    val isLoading: Boolean = true,
    val selectedCategory: Category? = null,
    val homeItemToDelete: HomeItem? = null,
    val error: String? = null
) {
    val filteredHomeItems: List<HomeItem>
        get() = if (selectedCategory != null) {
            homeItems.filter { it.category == selectedCategory }
        } else {
            homeItems
        }

    val isEmpty: Boolean
        get() = !isLoading && filteredHomeItems.isEmpty()

    fun getInterventionCount(homeItemId: Long): Int =
        interventionCounts[homeItemId] ?: 0
}
