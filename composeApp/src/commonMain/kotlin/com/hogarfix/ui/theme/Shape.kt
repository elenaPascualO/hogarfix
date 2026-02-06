package com.hogarfix.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // badges, chips pequeños
    small = RoundedCornerShape(8.dp),        // filter chips, mini cards
    medium = RoundedCornerShape(12.dp),      // cards estándar
    large = RoundedCornerShape(16.dp),       // diálogos, bottom sheets
    extraLarge = RoundedCornerShape(24.dp)   // FAB
)
