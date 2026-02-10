package com.hogarfix

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.ContactPhone
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hogarfix.ui.navigation.AppNavHost
import com.hogarfix.ui.navigation.NavRoute
import com.hogarfix.ui.theme.HogarFixTheme
import com.hogarfix.util.AppPreferences

data class BottomNavItem(
    val route: NavRoute,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(
        route = NavRoute.Home,
        label = "Inicio",
        selectedIcon = Icons.Filled.Dashboard,
        unselectedIcon = Icons.Outlined.Dashboard
    ),
    BottomNavItem(
        route = NavRoute.InterventionList,
        label = "Trabajos",
        selectedIcon = Icons.Filled.Construction,
        unselectedIcon = Icons.Outlined.Construction
    ),
    BottomNavItem(
        route = NavRoute.InventoryList,
        label = "Inventario",
        selectedIcon = Icons.Filled.Inventory2,
        unselectedIcon = Icons.Outlined.Inventory2
    ),
    BottomNavItem(
        route = NavRoute.ProfessionalList,
        label = "Contactos",
        selectedIcon = Icons.Filled.ContactPhone,
        unselectedIcon = Icons.Outlined.ContactPhone
    ),
    BottomNavItem(
        route = NavRoute.ReminderList,
        label = "Avisos",
        selectedIcon = Icons.Filled.NotificationsActive,
        unselectedIcon = Icons.Outlined.NotificationsActive
    )
)

@Composable
fun App() {
    HogarFixTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val startDestination = remember {
            if (AppPreferences().isOnboardingCompleted()) {
                NavRoute.Home
            } else {
                NavRoute.Onboarding
            }
        }

        val isOnboarding = currentDestination?.hasRoute(NavRoute.Onboarding::class) == true

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (!isOnboarding) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        tonalElevation = 0.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            val selected = currentDestination?.hierarchy?.any {
                                it.hasRoute(item.route::class)
                            } == true

                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (item.route == NavRoute.Home) {
                                        navController.popBackStack(NavRoute.Home, inclusive = false)
                                    } else {
                                        navController.navigate(item.route) {
                                            popUpTo(NavRoute.Home) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
