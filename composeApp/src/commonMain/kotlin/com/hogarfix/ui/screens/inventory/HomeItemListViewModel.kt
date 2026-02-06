package com.hogarfix.ui.screens.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.usecase.DeleteHomeItemUseCase
import com.hogarfix.domain.usecase.GetHomeItemsUseCase
import com.hogarfix.domain.usecase.GetInterventionsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeItemListViewModel(
    private val getHomeItemsUseCase: GetHomeItemsUseCase,
    private val deleteHomeItemUseCase: DeleteHomeItemUseCase,
    private val getInterventionsUseCase: GetInterventionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeItemListState())
    val state: StateFlow<HomeItemListState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadHomeItems()
    }

    private fun loadHomeItems() {
        viewModelScope.launch {
            getHomeItemsUseCase()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { homeItems ->
                    // Load intervention counts for each home item
                    val counts = mutableMapOf<Long, Int>()
                    homeItems.forEach { item ->
                        val interventions = getInterventionsUseCase.getByHomeItem(item.id).first()
                        counts[item.id] = interventions.size
                    }

                    _state.update {
                        it.copy(
                            homeItems = homeItems,
                            interventionCounts = counts,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onEvent(event: HomeItemListEvent) {
        when (event) {
            is HomeItemListEvent.AddHomeItem -> {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToForm(null))
                }
            }

            is HomeItemListEvent.EditHomeItem -> {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToForm(event.homeItem.id))
                }
            }

            is HomeItemListEvent.RequestDelete -> {
                _state.update { it.copy(homeItemToDelete = event.homeItem) }
            }

            is HomeItemListEvent.ConfirmDelete -> {
                val homeItem = _state.value.homeItemToDelete ?: return
                viewModelScope.launch {
                    try {
                        deleteHomeItemUseCase(homeItem.id)
                        _state.update { it.copy(homeItemToDelete = null) }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                homeItemToDelete = null,
                                error = "Error al eliminar: ${e.message}"
                            )
                        }
                    }
                }
            }

            is HomeItemListEvent.CancelDelete -> {
                _state.update { it.copy(homeItemToDelete = null) }
            }

            is HomeItemListEvent.FilterByCategory -> {
                _state.update { it.copy(selectedCategory = event.category) }
            }

            is HomeItemListEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    sealed interface NavigationEvent {
        data class NavigateToForm(val homeItemId: Long?) : NavigationEvent
    }
}
