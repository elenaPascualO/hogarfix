package com.hogarfix.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hogarfix.ui.components.InterventionCard
import com.hogarfix.ui.components.QuickActionButton
import com.hogarfix.ui.components.SummaryCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToInterventions: () -> Unit = {},
    onNavigateToInventory: () -> Unit = {},
    onNavigateToReminders: () -> Unit = {},
    onNavigateToForm: () -> Unit = {},
    onNavigateToDetail: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Saludo + Resumen
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Hola!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Resumen de tu hogar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            SummaryCard(
                icon = Icons.Outlined.AttachMoney,
                title = "Gasto este mes",
                value = formatExpense(state.monthlyExpense),
                subtitle = if (state.pendingCount > 0) {
                    "${state.pendingCount} trabajo${if (state.pendingCount > 1) "s" else ""} pendiente${if (state.pendingCount > 1) "s" else ""}"
                } else null
            )
        }

        // Acciones rápidas
        item {
            Text(
                text = "Acciones rápidas",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionButton(
                    icon = Icons.Outlined.Add,
                    label = "Trabajo",
                    onClick = onNavigateToForm
                )
                QuickActionButton(
                    icon = Icons.Outlined.Inventory2,
                    label = "Inventario",
                    onClick = onNavigateToInventory
                )
                QuickActionButton(
                    icon = Icons.Outlined.NotificationsActive,
                    label = "Recordatorio",
                    onClick = onNavigateToReminders
                )
            }
        }

        // Actividad reciente
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Actividad reciente",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                TextButton(onClick = onNavigateToInterventions) {
                    Text(text = "Ver todos")
                }
            }
        }

        if (state.recentInterventions.isEmpty()) {
            item {
                Text(
                    text = "No hay intervenciones registradas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        } else {
            items(
                items = state.recentInterventions,
                key = { it.id }
            ) { intervention ->
                InterventionCard(
                    intervention = intervention,
                    onClick = { onNavigateToDetail(intervention.id) }
                )
            }
        }

        // Espacio al final
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun formatExpense(amount: Double): String {
    val intPart = amount.toLong()
    val decimalPart = ((amount - intPart) * 100).toLong()
    return "${intPart}.${decimalPart.toString().padStart(2, '0')} €"
}
