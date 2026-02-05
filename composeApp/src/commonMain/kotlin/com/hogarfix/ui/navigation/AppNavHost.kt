package com.hogarfix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hogarfix.ui.screens.home.HomeScreen
import com.hogarfix.ui.screens.interventions.InterventionListScreen
import com.hogarfix.ui.screens.inventory.InventoryListScreen
import com.hogarfix.ui.screens.professionals.ProfessionalListScreen
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
            HomeScreen()
        }

        composable<NavRoute.InterventionList> {
            InterventionListScreen()
        }

        composable<NavRoute.InventoryList> {
            InventoryListScreen()
        }

        composable<NavRoute.ProfessionalList> {
            ProfessionalListScreen()
        }

        composable<NavRoute.ReminderList> {
            ReminderListScreen()
        }
    }
}
