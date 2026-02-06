package com.hogarfix.ui.screens.inventory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.model.HomeItem
import com.hogarfix.domain.usecase.DeleteHomeItemUseCase
import com.hogarfix.domain.usecase.GetHomeItemsUseCase
import com.hogarfix.domain.usecase.SaveHomeItemUseCase
import com.hogarfix.util.currentInstant
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeItemFormViewModel(
    private val getHomeItemsUseCase: GetHomeItemsUseCase,
    private val saveHomeItemUseCase: SaveHomeItemUseCase,
    private val deleteHomeItemUseCase: DeleteHomeItemUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val homeItemId: Long? = savedStateHandle.get<Long>("id")?.takeIf { it != -1L }

    private val _state = MutableStateFlow(HomeItemFormState())
    val state: StateFlow<HomeItemFormState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        homeItemId?.let { loadHomeItem(it) }
    }

    private fun loadHomeItem(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val homeItem = getHomeItemsUseCase.getById(id)
                if (homeItem != null) {
                    _state.update {
                        it.copy(
                            id = homeItem.id,
                            name = homeItem.name,
                            brand = homeItem.brand ?: "",
                            model = homeItem.model ?: "",
                            category = homeItem.category,
                            purchaseDate = homeItem.purchaseDate,
                            warrantyEndDate = homeItem.warrantyEndDate,
                            location = homeItem.location ?: "",
                            notes = homeItem.notes ?: "",
                            photoUris = homeItem.photoUris,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Elemento no encontrado") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: HomeItemFormEvent) {
        when (event) {
            is HomeItemFormEvent.NameChanged -> {
                _state.update { it.copy(name = event.name) }
            }

            is HomeItemFormEvent.BrandChanged -> {
                _state.update { it.copy(brand = event.brand) }
            }

            is HomeItemFormEvent.ModelChanged -> {
                _state.update { it.copy(model = event.model) }
            }

            is HomeItemFormEvent.CategoryChanged -> {
                _state.update { it.copy(category = event.category) }
            }

            is HomeItemFormEvent.PurchaseDateChanged -> {
                _state.update { it.copy(purchaseDate = event.date, showPurchaseDatePicker = false) }
            }

            is HomeItemFormEvent.WarrantyEndDateChanged -> {
                _state.update { it.copy(warrantyEndDate = event.date, showWarrantyDatePicker = false) }
            }

            is HomeItemFormEvent.LocationChanged -> {
                _state.update { it.copy(location = event.location) }
            }

            is HomeItemFormEvent.NotesChanged -> {
                _state.update { it.copy(notes = event.notes) }
            }

            is HomeItemFormEvent.PhotoAdded -> {
                _state.update { it.copy(pendingPhotos = it.pendingPhotos + event.photoBytes) }
            }

            is HomeItemFormEvent.PhotoRemoved -> {
                viewModelScope.launch {
                    try {
                        saveHomeItemUseCase.deletePhoto(event.uri)
                        _state.update { it.copy(photoUris = it.photoUris - event.uri) }
                    } catch (e: Exception) {
                        _state.update { it.copy(error = "Error al eliminar foto: ${e.message}") }
                    }
                }
            }

            is HomeItemFormEvent.PendingPhotoRemoved -> {
                _state.update {
                    it.copy(pendingPhotos = it.pendingPhotos.toMutableList().apply {
                        removeAt(event.index)
                    })
                }
            }

            is HomeItemFormEvent.ShowPurchaseDatePicker -> {
                _state.update { it.copy(showPurchaseDatePicker = true) }
            }

            is HomeItemFormEvent.HidePurchaseDatePicker -> {
                _state.update { it.copy(showPurchaseDatePicker = false) }
            }

            is HomeItemFormEvent.ShowWarrantyDatePicker -> {
                _state.update { it.copy(showWarrantyDatePicker = true) }
            }

            is HomeItemFormEvent.HideWarrantyDatePicker -> {
                _state.update { it.copy(showWarrantyDatePicker = false) }
            }

            is HomeItemFormEvent.ClearPurchaseDate -> {
                _state.update { it.copy(purchaseDate = null) }
            }

            is HomeItemFormEvent.ClearWarrantyDate -> {
                _state.update { it.copy(warrantyEndDate = null) }
            }

            is HomeItemFormEvent.Save -> {
                saveHomeItem()
            }

            is HomeItemFormEvent.RequestDelete -> {
                _state.update { it.copy(showDeleteConfirmation = true) }
            }

            is HomeItemFormEvent.ConfirmDelete -> {
                deleteHomeItem()
            }

            is HomeItemFormEvent.CancelDelete -> {
                _state.update { it.copy(showDeleteConfirmation = false) }
            }

            is HomeItemFormEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun deleteHomeItem() {
        val id = _state.value.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, showDeleteConfirmation = false) }
            try {
                deleteHomeItemUseCase(id)
                _navigationEvent.emit(NavigationEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, error = "Error al eliminar: ${e.message}") }
            }
        }
    }

    private fun saveHomeItem() {
        val currentState = _state.value
        if (!currentState.isValid) {
            _state.update { it.copy(error = "Por favor, completa los campos requeridos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val now = currentInstant()
                val homeItem = HomeItem(
                    id = currentState.id ?: 0,
                    name = currentState.name.trim(),
                    brand = currentState.brand.takeIf { it.isNotBlank() },
                    model = currentState.model.takeIf { it.isNotBlank() },
                    category = currentState.category!!,
                    purchaseDate = currentState.purchaseDate,
                    warrantyEndDate = currentState.warrantyEndDate,
                    location = currentState.location.takeIf { it.isNotBlank() },
                    notes = currentState.notes.takeIf { it.isNotBlank() },
                    photoUris = currentState.photoUris,
                    createdAt = now,
                    updatedAt = now
                )

                val savedId = saveHomeItemUseCase(homeItem)

                // Save pending photos
                val newPhotoUris = currentState.pendingPhotos.map { photoBytes ->
                    saveHomeItemUseCase.savePhoto(savedId, photoBytes)
                }

                // Update home item with new photo URIs if there are any
                if (newPhotoUris.isNotEmpty()) {
                    val updatedHomeItem = homeItem.copy(
                        id = savedId,
                        photoUris = currentState.photoUris + newPhotoUris
                    )
                    saveHomeItemUseCase(updatedHomeItem)
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
