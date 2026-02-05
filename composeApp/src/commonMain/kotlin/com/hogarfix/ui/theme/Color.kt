package com.hogarfix.ui.theme

import androidx.compose.ui.graphics.Color

// Primary colors
val Primary = Color(0xFF1565C0)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFD1E4FF)
val OnPrimaryContainer = Color(0xFF001D36)

// Secondary colors
val Secondary = Color(0xFF535F70)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFD7E3F8)
val OnSecondaryContainer = Color(0xFF101C2B)

// Tertiary colors
val Tertiary = Color(0xFF6B5778)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFFF3DAFF)
val OnTertiaryContainer = Color(0xFF251432)

// Error colors
val Error = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF410002)

// Background colors
val Background = Color(0xFFFDFCFF)
val OnBackground = Color(0xFF1A1C1E)
val Surface = Color(0xFFFDFCFF)
val OnSurface = Color(0xFF1A1C1E)
val SurfaceVariant = Color(0xFFDFE2EB)
val OnSurfaceVariant = Color(0xFF43474E)

// Outline
val Outline = Color(0xFF73777F)
val OutlineVariant = Color(0xFFC3C6CF)

// Category colors
object CategoryColors {
    val Plumbing = Color(0xFF1976D2)        // Azul
    val Electrical = Color(0xFFFFC107)      // Amarillo
    val Appliances = Color(0xFF757575)      // Gris
    val Painting = Color(0xFFE91E63)        // Rosa
    val Locksmith = Color(0xFF795548)       // Marron
    val Hvac = Color(0xFF00BCD4)            // Celeste
    val Carpentry = Color(0xFF8D6E63)       // Madera
    val Garden = Color(0xFF4CAF50)          // Verde
    val Cleaning = Color(0xFF9C27B0)        // Lila
    val Roofing = Color(0xFFF44336)         // Rojo
    val Flooring = Color(0xFFBCAAA4)        // Marron claro
    val WindowsDoors = Color(0xFF616161)    // Gris oscuro
    val Other = Color(0xFF9E9E9E)           // Gris
}

// Status colors
object StatusColors {
    val Pending = Color(0xFFFFA726)         // Naranja
    val InProgress = Color(0xFF42A5F5)      // Azul
    val Completed = Color(0xFF66BB6A)       // Verde
}

// Reminder urgency colors
object ReminderColors {
    val Overdue = Color(0xFFEF5350)         // Rojo
    val Soon = Color(0xFFFFA726)            // Amarillo/Naranja
    val OnTrack = Color(0xFF66BB6A)         // Verde
}