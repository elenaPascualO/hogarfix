package com.hogarfix.ui.screens.interventions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.Intervention
import com.hogarfix.domain.usecase.DeleteInterventionUseCase
import com.hogarfix.domain.usecase.GetInterventionsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InterventionListViewModel(
    private val getInterventionsUseCase: GetInterventionsUseCase,
    private val deleteInterventionUseCase: DeleteInterventionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InterventionListState())
    val state: StateFlow<InterventionListState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadInterventions()
    }

    private fun loadInterventions() {
        viewModelScope.launch {
            getInterventionsUseCase()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { interventions ->
                    _state.update {
                        it.copy(
                            interventions = interventions,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onEvent(event: InterventionListEvent) {
        when (event) {
            is InterventionListEvent.AddIntervention -> {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToForm(null))
                }
            }

            is InterventionListEvent.EditIntervention -> {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToForm(event.intervention.id))
                }
            }

            is InterventionListEvent.RequestDelete -> {
                _state.update { it.copy(interventionToDelete = event.intervention) }
            }

            is InterventionListEvent.ConfirmDelete -> {
                val intervention = _state.value.interventionToDelete ?: return
                viewModelScope.launch {
                    try {
                        deleteInterventionUseCase(intervention.id)
                        _state.update { it.copy(interventionToDelete = null) }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                interventionToDelete = null,
                                error = "Error al eliminar: ${e.message}"
                            )
                        }
                    }
                }
            }

            is InterventionListEvent.CancelDelete -> {
                _state.update { it.copy(interventionToDelete = null) }
            }

            is InterventionListEvent.FilterByCategory -> {
                _state.update { it.copy(selectedCategory = event.category) }
            }

            is InterventionListEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    sealed interface NavigationEvent {
        data class NavigateToForm(val interventionId: Long?) : NavigationEvent
    }
}
