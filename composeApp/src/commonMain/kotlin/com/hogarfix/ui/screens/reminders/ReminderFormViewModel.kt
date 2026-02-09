package com.hogarfix.ui.screens.reminders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.model.Reminder
import com.hogarfix.domain.usecase.DeleteReminderUseCase
import com.hogarfix.domain.usecase.GetRemindersUseCase
import com.hogarfix.domain.usecase.SaveReminderUseCase
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

class ReminderFormViewModel(
    private val getRemindersUseCase: GetRemindersUseCase,
    private val saveReminderUseCase: SaveReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val reminderId: Long? = savedStateHandle.get<Long>("id")?.takeIf { it != -1L }

    private val _state = MutableStateFlow(
        ReminderFormState(
            nextDueDate = currentDate().plus(30, DateTimeUnit.DAY)
        )
    )
    val state: StateFlow<ReminderFormState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        reminderId?.let { loadReminder(it) }
    }

    private fun loadReminder(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val reminder = getRemindersUseCase.getById(id)
                if (reminder != null) {
                    val (value, unit) = decomposeInterval(reminder.intervalDays)
                    _state.update {
                        it.copy(
                            id = reminder.id,
                            title = reminder.title,
                            description = reminder.description ?: "",
                            category = reminder.category,
                            intervalValue = value,
                            intervalUnit = unit,
                            nextDueDate = reminder.nextDueDate,
                            homeItemId = reminder.homeItemId,
                            isActive = reminder.isActive,
                            lastCompletedDate = reminder.lastCompletedDate,
                            createdAt = reminder.createdAt,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Recordatorio no encontrado") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: ReminderFormEvent) {
        when (event) {
            is ReminderFormEvent.TitleChanged -> {
                _state.update { it.copy(title = event.title) }
            }

            is ReminderFormEvent.DescriptionChanged -> {
                _state.update { it.copy(description = event.description) }
            }

            is ReminderFormEvent.CategoryChanged -> {
                _state.update { it.copy(category = event.category) }
            }

            is ReminderFormEvent.IntervalValueChanged -> {
                val newValue = event.value.coerceAtLeast(1)
                _state.update { it.copy(intervalValue = newValue) }
            }

            is ReminderFormEvent.IntervalUnitChanged -> {
                _state.update { it.copy(intervalUnit = event.unit) }
            }

            is ReminderFormEvent.NextDueDateChanged -> {
                _state.update { it.copy(nextDueDate = event.date, showDatePicker = false) }
            }

            is ReminderFormEvent.IsActiveChanged -> {
                _state.update { it.copy(isActive = event.isActive) }
            }

            is ReminderFormEvent.ShowDatePicker -> {
                _state.update { it.copy(showDatePicker = true) }
            }

            is ReminderFormEvent.HideDatePicker -> {
                _state.update { it.copy(showDatePicker = false) }
            }

            is ReminderFormEvent.Save -> {
                saveReminder()
            }

            is ReminderFormEvent.RequestDelete -> {
                _state.update { it.copy(showDeleteConfirmation = true) }
            }

            is ReminderFormEvent.ConfirmDelete -> {
                deleteReminder()
            }

            is ReminderFormEvent.CancelDelete -> {
                _state.update { it.copy(showDeleteConfirmation = false) }
            }

            is ReminderFormEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun deleteReminder() {
        val id = _state.value.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, showDeleteConfirmation = false) }
            try {
                deleteReminderUseCase(id)
                _navigationEvent.emit(NavigationEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, error = "Error al eliminar: ${e.message}") }
            }
        }
    }

    private fun saveReminder() {
        val currentState = _state.value
        if (!currentState.isValid) {
            _state.update { it.copy(error = "Por favor, completa los campos requeridos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val reminder = Reminder(
                    id = currentState.id ?: 0,
                    title = currentState.title.trim(),
                    description = currentState.description.takeIf { it.isNotBlank() },
                    intervalDays = currentState.intervalDays,
                    nextDueDate = currentState.nextDueDate!!,
                    homeItemId = currentState.homeItemId,
                    category = currentState.category!!,
                    isActive = currentState.isActive,
                    lastCompletedDate = currentState.lastCompletedDate,
                    createdAt = currentState.createdAt ?: currentInstant()
                )

                saveReminderUseCase(reminder)
                _state.update { it.copy(isSaving = false) }
                _navigationEvent.emit(NavigationEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, error = "Error al guardar: ${e.message}") }
            }
        }
    }

    companion object {
        fun decomposeInterval(days: Int): Pair<Int, IntervalUnit> {
            return when {
                days >= 365 && days % 365 == 0 -> Pair(days / 365, IntervalUnit.YEARS)
                days >= 30 && days % 30 == 0 -> Pair(days / 30, IntervalUnit.MONTHS)
                days >= 7 && days % 7 == 0 -> Pair(days / 7, IntervalUnit.WEEKS)
                else -> Pair(days, IntervalUnit.DAYS)
            }
        }
    }

    sealed interface NavigationEvent {
        data object NavigateBack : NavigationEvent
    }
}
