package com.hogarfix.ui.screens.home

import com.hogarfix.domain.model.Intervention

data class HomeState(
    val monthlyExpense: Double = 0.0,
    val pendingCount: Int = 0,
    val recentInterventions: List<Intervention> = emptyList(),
    val isLoading: Boolean = true
)
