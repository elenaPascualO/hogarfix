package com.hogarfix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hogarfix.ui.screens.home.HomeScreen
import com.hogarfix.ui.screens.interventions.InterventionFormScreen
import com.hogarfix.ui.screens.interventions.InterventionListScreen
import com.hogarfix.ui.screens.inventory.HomeItemFormScreen
import com.hogarfix.ui.screens.inventory.InventoryListScreen
import com.hogarfix.ui.screens.professionals.ProfessionalFormScreen
import com.hogarfix.ui.screens.professionals.ProfessionalListScreen
import com.hogarfix.ui.screens.reminders.ReminderFormScreen
import com.hogarfix.ui.screens.reminders.ReminderListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Home,
        modifier = modifier
    ) {
        composable<NavRoute.Home> {
            HomeScreen(
                onNavigateToInterventions = {
                    navController.navigate(NavRoute.InterventionList)
                },
                onNavigateToInventory = {
                    navController.navigate(NavRoute.InventoryList)
                },
                onNavigateToReminders = {
                    navController.navigate(NavRoute.ReminderList)
                },
                onNavigateToForm = {
                    navController.navigate(NavRoute.InterventionForm(id = null))
                },
                onNavigateToDetail = { interventionId ->
                    navController.navigate(NavRoute.InterventionForm(id = interventionId))
                }
            )
        }

        composable<NavRoute.InterventionList> {
            InterventionListScreen(
                onNavigateToForm = { interventionId ->
                    navController.navigate(NavRoute.InterventionForm(id = interventionId))
                }
            )
        }

        composable<NavRoute.InterventionForm> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.InterventionForm>()
            InterventionFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<NavRoute.InventoryList> {
            InventoryListScreen(
                onNavigateToForm = { homeItemId ->
                    navController.navigate(NavRoute.HomeItemForm(id = homeItemId))
                }
            )
        }

        composable<NavRoute.HomeItemForm> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.HomeItemForm>()
            HomeItemFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<NavRoute.ProfessionalList> {
            ProfessionalListScreen(
                onNavigateToForm = { professionalId ->
                    navController.navigate(NavRoute.ProfessionalForm(id = professionalId))
                }
            )
        }

        composable<NavRoute.ProfessionalForm> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.ProfessionalForm>()
            ProfessionalFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<NavRoute.ReminderList> {
            ReminderListScreen(
                onNavigateToForm = { reminderId ->
                    navController.navigate(NavRoute.ReminderForm(id = reminderId))
                }
            )
        }

        composable<NavRoute.ReminderForm> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.ReminderForm>()
            ReminderFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
