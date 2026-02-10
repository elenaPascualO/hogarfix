package com.hogarfix.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object Onboarding : NavRoute

    @Serializable
    data object Home : NavRoute

    @Serializable
    data object InterventionList : NavRoute

    @Serializable
    data class InterventionForm(val id: Long? = null) : NavRoute

    @Serializable
    data object InventoryList : NavRoute

    @Serializable
    data class HomeItemForm(val id: Long? = null) : NavRoute

    @Serializable
    data object ProfessionalList : NavRoute

    @Serializable
    data class ProfessionalForm(val id: Long? = null) : NavRoute

    @Serializable
    data object ReminderList : NavRoute

    @Serializable
    data class ReminderForm(val id: Long? = null) : NavRoute

    @Serializable
    data object Search : NavRoute
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
