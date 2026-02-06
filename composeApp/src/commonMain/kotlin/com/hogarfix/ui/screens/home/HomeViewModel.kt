package com.hogarfix.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.model.Status
import com.hogarfix.domain.usecase.GetInterventionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.hogarfix.util.currentDate

class HomeViewModel(
    private val getInterventionsUseCase: GetInterventionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            getInterventionsUseCase().collect { interventions ->
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

                _state.update {
                    it.copy(
                        monthlyExpense = monthlyExpense,
                        pendingCount = pendingCount,
                        recentInterventions = recentInterventions,
                        isLoading = false
                    )
                }
            }
        }
    }
}
