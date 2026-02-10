package com.hogarfix.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.model.HousingType
import com.hogarfix.domain.model.OnboardingTemplates
import com.hogarfix.domain.model.Reminder
import com.hogarfix.domain.usecase.SaveReminderUseCase
import com.hogarfix.util.AppPreferences
import com.hogarfix.util.NotificationScheduler
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

data class OnboardingState(
    val selectedHousingType: HousingType? = null,
    val isSaving: Boolean = false
)

class OnboardingViewModel(
    private val saveReminderUseCase: SaveReminderUseCase,
    private val appPreferences: AppPreferences,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    fun selectHousingType(type: HousingType) {
        _state.update { it.copy(selectedHousingType = type) }
    }

    fun confirm() {
        val housingType = _state.value.selectedHousingType ?: return
        notificationScheduler.requestPermission { /* result ignored — best effort */ }
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val templates = OnboardingTemplates.getTemplates(housingType)
                val today = currentDate()
                val now = currentInstant()

                templates.forEach { template ->
                    val reminder = Reminder(
                        title = template.title,
                        description = template.description,
                        category = template.category,
                        intervalDays = template.intervalDays,
                        nextDueDate = today.plus(template.intervalDays, DateTimeUnit.DAY),
                        isActive = true,
                        createdAt = now
                    )
                    saveReminderUseCase(reminder)
                }

                appPreferences.setOnboardingCompleted(true)
                _navigationEvent.emit(NavigationEvent.NavigateToHome)
            } catch (_: Exception) {
                appPreferences.setOnboardingCompleted(true)
                _navigationEvent.emit(NavigationEvent.NavigateToHome)
            }
        }
    }

    fun skip() {
        appPreferences.setOnboardingCompleted(true)
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToHome)
        }
    }

    sealed interface NavigationEvent {
        data object NavigateToHome : NavigationEvent
    }
}
