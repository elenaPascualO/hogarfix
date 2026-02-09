package com.hogarfix.ui.screens.professionals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.model.Professional
import com.hogarfix.domain.usecase.DeleteProfessionalUseCase
import com.hogarfix.domain.usecase.GetProfessionalsUseCase
import com.hogarfix.domain.usecase.SaveProfessionalUseCase
import com.hogarfix.util.currentInstant
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfessionalFormViewModel(
    private val getProfessionalsUseCase: GetProfessionalsUseCase,
    private val saveProfessionalUseCase: SaveProfessionalUseCase,
    private val deleteProfessionalUseCase: DeleteProfessionalUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val professionalId: Long? = savedStateHandle.get<Long>("id")?.takeIf { it != -1L }

    private val _state = MutableStateFlow(ProfessionalFormState())
    val state: StateFlow<ProfessionalFormState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        professionalId?.let { loadProfessional(it) }
    }

    private fun loadProfessional(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val professional = getProfessionalsUseCase.getById(id)
                if (professional != null) {
                    _state.update {
                        it.copy(
                            id = professional.id,
                            name = professional.name,
                            phone = professional.phone ?: "",
                            email = professional.email ?: "",
                            specialty = professional.specialty,
                            personalRating = professional.personalRating,
                            notes = professional.notes ?: "",
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Profesional no encontrado") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: ProfessionalFormEvent) {
        when (event) {
            is ProfessionalFormEvent.NameChanged -> {
                _state.update { it.copy(name = event.name) }
            }

            is ProfessionalFormEvent.PhoneChanged -> {
                _state.update { it.copy(phone = event.phone) }
            }

            is ProfessionalFormEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
            }

            is ProfessionalFormEvent.SpecialtyChanged -> {
                _state.update { it.copy(specialty = event.specialty) }
            }

            is ProfessionalFormEvent.RatingChanged -> {
                _state.update { it.copy(personalRating = event.rating) }
            }

            is ProfessionalFormEvent.NotesChanged -> {
                _state.update { it.copy(notes = event.notes) }
            }

            is ProfessionalFormEvent.Save -> {
                saveProfessional()
            }

            is ProfessionalFormEvent.RequestDelete -> {
                _state.update { it.copy(showDeleteConfirmation = true) }
            }

            is ProfessionalFormEvent.ConfirmDelete -> {
                deleteProfessional()
            }

            is ProfessionalFormEvent.CancelDelete -> {
                _state.update { it.copy(showDeleteConfirmation = false) }
            }

            is ProfessionalFormEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun deleteProfessional() {
        val id = _state.value.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, showDeleteConfirmation = false) }
            try {
                deleteProfessionalUseCase(id)
                _navigationEvent.emit(NavigationEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, error = "Error al eliminar: ${e.message}") }
            }
        }
    }

    private fun saveProfessional() {
        val currentState = _state.value
        if (!currentState.isValid) {
            _state.update { it.copy(error = "Por favor, completa los campos requeridos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val now = currentInstant()
                val professional = Professional(
                    id = currentState.id ?: 0,
                    name = currentState.name.trim(),
                    phone = currentState.phone.takeIf { it.isNotBlank() },
                    email = currentState.email.takeIf { it.isNotBlank() },
                    specialty = currentState.specialty!!,
                    personalRating = currentState.personalRating,
                    notes = currentState.notes.takeIf { it.isNotBlank() },
                    createdAt = now
                )

                saveProfessionalUseCase(professional)

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
