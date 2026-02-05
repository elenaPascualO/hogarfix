package com.hogarfix.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object Home : NavRoute

    @Serializable
    data object InterventionList : NavRoute

    @Serializable
    data object InventoryList : NavRoute

    @Serializable
    data object ProfessionalList : NavRoute

    @Serializable
    data object ReminderList : NavRoute
}

enum class BottomNavItem(
    val route: NavRoute,
    val label: String,
    val icon: String // Will use Material Icons in actual implementation
) {
    HOME(NavRoute.Home, "Inicio", "home"),
    INTERVENTIONS(NavRoute.InterventionList, "Trabajos", "build"),
    INVENTORY(NavRoute.InventoryList, "Inventario", "inventory_2"),
    PROFESSIONALS(NavRoute.ProfessionalList, "Contactos", "contacts"),
    REMINDERS(NavRoute.ReminderList, "Recordatorios", "notifications")
}
