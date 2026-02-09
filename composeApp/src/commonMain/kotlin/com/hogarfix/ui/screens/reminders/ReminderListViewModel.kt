package com.hogarfix.ui.screens.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.usecase.CompleteReminderUseCase
import com.hogarfix.domain.usecase.DeleteReminderUseCase
import com.hogarfix.domain.usecase.GetRemindersUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReminderListViewModel(
    private val getRemindersUseCase: GetRemindersUseCase,
    private val completeReminderUseCase: CompleteReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ReminderListState())
    val state: StateFlow<ReminderListState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadReminders()
    }

    private fun loadReminders() {
        viewModelScope.launch {
            getRemindersUseCase()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { reminders ->
                    _state.update {
                        it.copy(
                            reminders = reminders,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onEvent(event: ReminderListEvent) {
        when (event) {
            is ReminderListEvent.AddReminder -> {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToForm(null))
                }
            }

            is ReminderListEvent.EditReminder -> {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToForm(event.reminder.id))
                }
            }

            is ReminderListEvent.CompleteReminder -> {
                viewModelScope.launch {
                    try {
                        completeReminderUseCase(event.id)
                    } catch (e: Exception) {
                        _state.update { it.copy(error = "Error al completar: ${e.message}") }
                    }
                }
            }

            is ReminderListEvent.RequestDelete -> {
                _state.update { it.copy(reminderToDelete = event.reminder) }
            }

            is ReminderListEvent.ConfirmDelete -> {
                val reminder = _state.value.reminderToDelete ?: return
                viewModelScope.launch {
                    try {
                        deleteReminderUseCase(reminder.id)
                        _state.update { it.copy(reminderToDelete = null) }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                reminderToDelete = null,
                                error = "Error al eliminar: ${e.message}"
                            )
                        }
                    }
                }
            }

            is ReminderListEvent.CancelDelete -> {
                _state.update { it.copy(reminderToDelete = null) }
            }

            is ReminderListEvent.FilterByCategory -> {
                _state.update { it.copy(selectedCategory = event.category) }
            }

            is ReminderListEvent.ClearError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    sealed interface NavigationEvent {
        data class NavigateToForm(val reminderId: Long?) : NavigationEvent
    }
}
