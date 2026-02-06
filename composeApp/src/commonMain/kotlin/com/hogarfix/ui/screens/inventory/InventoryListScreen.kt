package com.hogarfix.ui.screens.inventory

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
import androidx.compose.material.icons.outlined.Inventory2
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
import com.hogarfix.ui.components.HomeItemCard
import com.hogarfix.ui.components.SwipeToDeleteContainer
import com.hogarfix.ui.components.getCategoryIcon
import com.hogarfix.ui.components.getCategoryLabel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryListScreen(
    onNavigateToForm: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeItemListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is HomeItemListViewModel.NavigationEvent.NavigateToForm -> {
                    onNavigateToForm(event.homeItemId)
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(HomeItemListEvent.ClearError)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Inventario") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(HomeItemListEvent.AddHomeItem) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Anadir elemento"
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
                    viewModel.onEvent(HomeItemListEvent.FilterByCategory(category))
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
                        icon = Icons.Outlined.Inventory2,
                        title = if (state.selectedCategory != null) {
                            "Sin elementos en ${getCategoryLabel(state.selectedCategory!!)}"
                        } else {
                            "Sin elementos"
                        },
                        message = if (state.selectedCategory != null) {
                            "No hay electrodomesticos registrados en esta categoria"
                        } else {
                            "Registra tu primer electrodomestico o elemento del hogar"
                        },
                        actionLabel = if (state.selectedCategory == null) "Anadir elemento" else null,
                        onAction = if (state.selectedCategory == null) {
                            { viewModel.onEvent(HomeItemListEvent.AddHomeItem) }
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
                            items = state.filteredHomeItems,
                            key = { it.id }
                        ) { homeItem ->
                            SwipeToDeleteContainer(
                                onDelete = {
                                    viewModel.onEvent(HomeItemListEvent.RequestDelete(homeItem))
                                }
                            ) {
                                HomeItemCard(
                                    homeItem = homeItem,
                                    interventionCount = state.getInterventionCount(homeItem.id),
                                    onClick = {
                                        viewModel.onEvent(HomeItemListEvent.EditHomeItem(homeItem))
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
    state.homeItemToDelete?.let { homeItem ->
        DeleteConfirmationDialog(
            title = "Eliminar elemento",
            message = "¿Estas seguro de que quieres eliminar \"${homeItem.name}\"? Esta accion no se puede deshacer.",
            onConfirm = { viewModel.onEvent(HomeItemListEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(HomeItemListEvent.CancelDelete) }
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
