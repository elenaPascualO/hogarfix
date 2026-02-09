package com.hogarfix.ui.screens.professionals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hogarfix.domain.model.Category
import com.hogarfix.ui.components.DeleteConfirmationDialog
import com.hogarfix.ui.components.EmptyStateView
import com.hogarfix.ui.components.ProfessionalCard
import com.hogarfix.ui.components.SwipeToDeleteContainer
import com.hogarfix.ui.components.getCategoryIcon
import com.hogarfix.ui.components.getCategoryLabel
import com.hogarfix.util.openDialer
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalListScreen(
    onNavigateToForm: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfessionalListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ProfessionalListViewModel.NavigationEvent.NavigateToForm -> {
                    onNavigateToForm(event.professionalId)
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(ProfessionalListEvent.ClearError)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Profesionales") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(ProfessionalListEvent.AddProfessional) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Anadir profesional"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category filter
            CategoryFilterRow(
                selectedCategory = state.selectedCategory,
                onCategorySelected = { category ->
                    viewModel.onEvent(ProfessionalListEvent.FilterByCategory(category))
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.isEmpty -> {
                    EmptyStateView(
                        icon = Icons.Outlined.Contacts,
                        title = if (state.selectedCategory != null) {
                            "Sin profesionales en ${getCategoryLabel(state.selectedCategory!!)}"
                        } else {
                            "Sin profesionales"
                        },
                        message = if (state.selectedCategory != null) {
                            "No hay profesionales registrados en esta categoria"
                        } else {
                            "Registra tu primer profesional o contacto"
                        },
                        actionLabel = if (state.selectedCategory == null) "Anadir profesional" else null,
                        onAction = if (state.selectedCategory == null) {
                            { viewModel.onEvent(ProfessionalListEvent.AddProfessional) }
                        } else null
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.filteredProfessionals,
                            key = { it.id }
                        ) { professional ->
                            SwipeToDeleteContainer(
                                onDelete = {
                                    viewModel.onEvent(ProfessionalListEvent.RequestDelete(professional))
                                }
                            ) {
                                ProfessionalCard(
                                    professional = professional,
                                    interventionCount = state.getInterventionCount(professional.id),
                                    onClick = {
                                        viewModel.onEvent(ProfessionalListEvent.EditProfessional(professional))
                                    },
                                    onCallClick = professional.phone?.let { phone ->
                                        { openDialer(phone) }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    state.professionalToDelete?.let { professional ->
        DeleteConfirmationDialog(
            title = "Eliminar profesional",
            message = "¿Estas seguro de que quieres eliminar \"${professional.name}\"? Esta accion no se puede deshacer.",
            onConfirm = { viewModel.onEvent(ProfessionalListEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(ProfessionalListEvent.CancelDelete) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryFilterRow(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Todos") }
            )
        }
        items(Category.entries.size) { index ->
            val category = Category.entries[index]
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                leadingIcon = {
                    Icon(
                        imageVector = getCategoryIcon(category),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                label = { Text(getCategoryLabel(category)) }
            )
        }
    }
}
