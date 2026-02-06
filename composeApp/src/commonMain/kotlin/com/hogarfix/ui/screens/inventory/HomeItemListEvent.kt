package com.hogarfix.ui.screens.inventory

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.HomeItem

sealed interface HomeItemListEvent {
    data object AddHomeItem : HomeItemListEvent
    data class EditHomeItem(val homeItem: HomeItem) : HomeItemListEvent
    data class RequestDelete(val homeItem: HomeItem) : HomeItemListEvent
    data object ConfirmDelete : HomeItemListEvent
    data object CancelDelete : HomeItemListEvent
    data class FilterByCategory(val category: Category?) : HomeItemListEvent
    data object ClearError : HomeItemListEvent
}
