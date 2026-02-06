package com.hogarfix.ui.screens.interventions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.model.Intervention
import com.hogarfix.domain.usecase.DeleteInterventionUseCase
import com.hogarfix.domain.usecase.GetInterventionsUseCase
import com.hogarfix.domain.usecase.SaveInterventionUseCase
import com.hogarfix.util.currentDate
import com.hogarfix.util.currentInstant
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InterventionFormViewModel(
    private val getInterventionsUseCase: GetInterventionsUseCase,
    private val saveInterventionUseCase: SaveInterventionUseCase,
    private val deleteInterventionUseCase: DeleteInterventionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val interventionId: Long? = savedStateHandle.get<Long>("id")?.takeIf { it != -1L }

    private val _state = MutableStateFlow(InterventionFormState(date = currentDate()))
    val state: StateFlow<InterventionFormState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        interventionId?.let { loadIntervention(it) }
    }

    private fun loadIntervention(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val intervention = getInterventionsUseCase.getById(id)
                if (intervention != null) {
                    _state.update {
                        it.copy(
                            id = intervention.id,
                            title = intervention.title,
                            description = intervention.description ?: "",
                            date = intervention.date,
                            category = intervention.category,
                            laborCost = intervention.laborCost?.toString() ?: "",
                            materialCost = intervention.materialCost?.toString() ?: "",
                            status = intervention.status,
                            doneBy = intervention.doneBy,
                            professionalId = intervention.professionalId,
                            homeItemId = intervention.homeItemId,
                            photoUris = intervention.photoUris,
                            notes = intervention.notes ?: "",
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Intervencion no encontrada") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: InterventionFormEvent) {
        when (event) {
            is InterventionFormEvent.TitleChanged -> {
                _state.update { it.copy(title = event.title) }
            }

            is InterventionFormEvent.DescriptionChanged -> {
                _state.update { it.copy(description = event.description) }
            }

            is InterventionFormEvent.DateChanged -> {
                _state.update { it.copy(date = event.date, showDatePicker = false) }
            }

            is InterventionFormEvent.CategoryChanged -> {
                _state.update { it.copy(category = event.category) }
            }

            is InterventionFormEvent.LaborCostChanged -> {
                _state.update { it.copy(laborCost = event.cost) }
            }

            is InterventionFormEvent.MaterialCostChanged -> {
                _state.update { it.copy(materialCost = event.cost) }
            }

            is InterventionFormEvent.StatusChanged -> {
                _state.update { it.copy(status = event.status) }
            }

            is InterventionFormEvent.DoneByChanged -> {
                _state.update { it.copy(doneBy = event.doneBy) }
            }

            is InterventionFormEvent.NotesChanged -> {
                _state.update { it.copy(notes = event.notes) }
            }

            is InterventionFormEvent.PhotoAdded -> {
                _state.update { it.copy(pendingPhotos = it.pendingPhotos + event.photoBytes) }
            }

            is InterventionFormEvent.PhotoRemoved -> {
                viewModelScope.launch {
                    try {
                        saveInterventionUseCase.deletePhoto(event.uri)
                        _state.update { it.copy(photoUris = it.photoUris - event.uri) }
                    } catch (e: Exception) {
                        _state.update { it.copy(error = "Error al eliminar foto: ${e.message}") }
                    }
                }
            }

            is InterventionFormEvent.PendingPhotoRemoved -> {
                _state.update {
                    it.copy(pendingPhotos = it.pendingPhotos.toMutableList().apply {
                        removeAt(event.index)
                    })
                }
            }

            is InterventionFormEvent.ShowDatePicker -> {
                _state.update { it.copy(showDatePicker = true) }
            }

            is InterventionFormEvent.HideDatePicker -> {
                _state.update { it.copy(showDatePicker = false) }
            }

            is InterventionFormEvent.Save -> {
                saveIntervention()
            }

            is InterventionFormEvent.RequestDelete -> {
                _state.update { it.copy(showDeleteConfirmation = true) }
            }

            is InterventionFormEvent.ConfirmDelete -> {
                deleteIntervention()
            }

            is InterventionFormEvent.CancelDelete -> {
                _state.update { it.copy(showDeleteConfirmation = false) }
            }

            is InterventionFormEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun deleteIntervention() {
        val id = _state.value.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, showDeleteConfirmation = false) }
            try {
                deleteInterventionUseCase(id)
                _navigationEvent.emit(NavigationEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, error = "Error al eliminar: ${e.message}") }
            }
        }
    }

    private fun saveIntervention() {
        val currentState = _state.value
        if (!currentState.isValid) {
            _state.update { it.copy(error = "Por favor, completa los campos requeridos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val now = currentInstant()
                val intervention = Intervention(
                    id = currentState.id ?: 0,
                    title = currentState.title.trim(),
                    description = currentState.description.takeIf { it.isNotBlank() },
                    date = currentState.date!!,
                    category = currentState.category!!,
                    laborCost = currentState.laborCostDouble,
                    materialCost = currentState.materialCostDouble,
                    status = currentState.status,
                    doneBy = currentState.doneBy,
                    professionalId = currentState.professionalId,
                    homeItemId = currentState.homeItemId,
                    photoUris = currentState.photoUris,
                    notes = currentState.notes.takeIf { it.isNotBlank() },
                    createdAt = now,
                    updatedAt = now
                )

                val savedId = saveInterventionUseCase(intervention)

                // Save pending photos
                val newPhotoUris = currentState.pendingPhotos.map { photoBytes ->
                    saveInterventionUseCase.savePhoto(savedId, photoBytes)
                }

                // Update intervention with new photo URIs if there are any
                if (newPhotoUris.isNotEmpty()) {
                    val updatedIntervention = intervention.copy(
                        id = savedId,
                        photoUris = currentState.photoUris + newPhotoUris
                    )
                    saveInterventionUseCase(updatedIntervention)
                }

                _state.update { it.copy(isSaving = false) }
                _navigationEvent.emit(NavigationEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, error = "Error al guardar: ${e.message}") }
            }
        }
    }

    sealed interface NavigationEvent {
        data object NavigateBack : NavigationEvent
    }
}
