package com.hogarfix.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.model.Status
import com.hogarfix.domain.usecase.GetInterventionsUseCase
import com.hogarfix.domain.usecase.GetRemindersUseCase
import com.hogarfix.util.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.hogarfix.util.currentDate

class HomeViewModel(
    private val getInterventionsUseCase: GetInterventionsUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadDashboardData()
        rescheduleNotifications()
    }

    private fun rescheduleNotifications() {
        viewModelScope.launch {
            try {
                val activeReminders = getRemindersUseCase().first()
                activeReminders.forEach { reminder ->
                    notificationScheduler.schedule(reminder)
                }
            } catch (_: Exception) {
                // Silent fail — rescheduling is best-effort
            }
        }
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            combine(
                getInterventionsUseCase(),
                getRemindersUseCase.getOverdue(),
                getRemindersUseCase.getUpcoming(7)
            ) { interventions, overdueReminders, upcomingReminders ->
                val today = currentDate()
                val currentMonth = today.monthNumber
                val currentYear = today.year

                // Gasto del mes actual
                val monthlyExpense = interventions
                    .filter { it.date.monthNumber == currentMonth && it.date.year == currentYear }
                    .sumOf { it.totalCost }

                // Conteo de pendientes
                val pendingCount = interventions.count { it.status == Status.PENDING }

                // 3 intervenciones más recientes
                val recentInterventions = interventions
                    .sortedByDescending { it.date }
                    .take(3)

                HomeState(
                    monthlyExpense = monthlyExpense,
                    pendingCount = pendingCount,
                    recentInterventions = recentInterventions,
                    overdueRemindersCount = overdueReminders.size,
                    upcomingReminders = upcomingReminders.take(3),
                    isLoading = false
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }
}
