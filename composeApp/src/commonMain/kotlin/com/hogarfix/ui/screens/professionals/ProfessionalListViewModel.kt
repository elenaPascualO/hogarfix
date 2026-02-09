package com.hogarfix.ui.screens.professionals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.usecase.DeleteProfessionalUseCase
import com.hogarfix.domain.usecase.GetInterventionsUseCase
import com.hogarfix.domain.usecase.GetProfessionalsUseCase
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

class ProfessionalListViewModel(
    private val getProfessionalsUseCase: GetProfessionalsUseCase,
    private val deleteProfessionalUseCase: DeleteProfessionalUseCase,
    private val getInterventionsUseCase: GetInterventionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfessionalListState())
    val state: StateFlow<ProfessionalListState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadProfessionals()
    }

    private fun loadProfessionals() {
        viewModelScope.launch {
            getProfessionalsUseCase()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { professionals ->
                    val counts = mutableMapOf<Long, Int>()
                    professionals.forEach { professional ->
                        val interventions = getInterventionsUseCase.getByProfessional(professional.id).first()
                        counts[professional.id] = interventions.size
                    }

                    _state.update {
                        it.copy(
                            professionals = professionals,
                            interventionCounts = counts,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onEvent(event: ProfessionalListEvent) {
        when (event) {
            is ProfessionalListEvent.AddProfessional -> {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToForm(null))
                }
            }

            is ProfessionalListEvent.EditProfessional -> {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToForm(event.professional.id))
                }
            }

            is ProfessionalListEvent.RequestDelete -> {
                _state.update { it.copy(professionalToDelete = event.professional) }
            }

            is ProfessionalListEvent.ConfirmDelete -> {
                val professional = _state.value.professionalToDelete ?: return
                viewModelScope.launch {
                    try {
                        deleteProfessionalUseCase(professional.id)
                        _state.update { it.copy(professionalToDelete = null) }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                professionalToDelete = null,
                                error = "Error al eliminar: ${e.message}"
                            )
                        }
                    }
                }
            }

            is ProfessionalListEvent.CancelDelete -> {
                _state.update { it.copy(professionalToDelete = null) }
            }

            is ProfessionalListEvent.FilterByCategory -> {
                _state.update { it.copy(selectedCategory = event.category) }
            }

            is ProfessionalListEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    sealed interface NavigationEvent {
        data class NavigateToForm(val professionalId: Long?) : NavigationEvent
    }
}
