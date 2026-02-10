package com.hogarfix.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hogarfix.domain.model.HousingType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is OnboardingViewModel.NavigationEvent.NavigateToHome -> {
                    onNavigateToHome()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido a HogarFix",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selecciona tu tipo de vivienda para crear recordatorios de mantenimiento personalizados",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        HousingTypeCard(
            icon = Icons.Outlined.Apartment,
            title = "Piso / Apartamento",
            description = "6 recordatorios basicos de mantenimiento",
            isSelected = state.selectedHousingType == HousingType.APARTMENT,
            onClick = { viewModel.selectHousingType(HousingType.APARTMENT) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        HousingTypeCard(
            icon = Icons.Outlined.Home,
            title = "Casa con jardin",
            description = "8 recordatorios: basicos + jardin",
            isSelected = state.selectedHousingType == HousingType.HOUSE_WITH_GARDEN,
            onClick = { viewModel.selectHousingType(HousingType.HOUSE_WITH_GARDEN) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        HousingTypeCard(
            icon = Icons.Outlined.Pool,
            title = "Chalet con piscina",
            description = "10 recordatorios: basicos + jardin + piscina",
            isSelected = state.selectedHousingType == HousingType.VILLA_WITH_POOL,
            onClick = { viewModel.selectHousingType(HousingType.VILLA_WITH_POOL) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.confirm() },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.selectedHousingType != null && !state.isSaving
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp),
                    strokeWidth = 2.dp
                )
            }
            Text("Crear recordatorios")
        }

        TextButton(
            onClick = { viewModel.skip() },
            enabled = !state.isSaving
        ) {
            Text("Omitir por ahora")
        }
    }
}

@Composable
private fun HousingTypeCard(
    icon: ImageVector,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
