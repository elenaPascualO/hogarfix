package com.hogarfix.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================
// LIGHT THEME - Terracota cálido mediterráneo
// ============================================

// Primary colors - Terracota
val Primary = Color(0xFFD4754E)              // Terracota suave
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFFFDBCE)     // Melocotón claro
val OnPrimaryContainer = Color(0xFF3A0B00)

// Secondary colors - Verde salvia
val Secondary = Color(0xFF6B7B6A)            // Verde salvia
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFD0E8CF)   // Salvia claro
val OnSecondaryContainer = Color(0xFF0F1F10)

// Tertiary colors - Dorado cálido
val Tertiary = Color(0xFF7D6B3A)             // Dorado cálido
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFFFFF0C7)    // Dorado claro
val OnTertiaryContainer = Color(0xFF261A00)

// Error colors
val Error = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF410002)

// Background colors - Crema cálido
val Background = Color(0xFFFFF8F5)           // Crema cálido
val OnBackground = Color(0xFF231917)
val Surface = Color(0xFFFFF8F5)              // Crema cálido
val OnSurface = Color(0xFF231917)
val SurfaceVariant = Color(0xFFF5DED3)       // Melocotón suave
val OnSurfaceVariant = Color(0xFF53433D)

// Outline
val Outline = Color(0xFF85746B)              // Marrón suave
val OutlineVariant = Color(0xFFD8C2B8)       // Beige

// Surface tones (Material 3)
val SurfaceTint = Color(0xFFD4754E)
val SurfaceBright = Color(0xFFFFFBFF)
val SurfaceDim = Color(0xFFE5D7D0)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFFFF1EB)
val SurfaceContainer = Color(0xFFFBEBE3)
val SurfaceContainerHigh = Color(0xFFF5E5DD)
val SurfaceContainerHighest = Color(0xFFEFDFD7)

// ============================================
// DARK THEME - Marrón cálido (no gris frío)
// ============================================

val DarkPrimary = Color(0xFFFFB599)          // Terracota claro
val DarkOnPrimary = Color(0xFF5C1900)
val DarkPrimaryContainer = Color(0xFF7D2E0D)
val DarkOnPrimaryContainer = Color(0xFFFFDBCE)

val DarkSecondary = Color(0xFFB5CCB3)
val DarkOnSecondary = Color(0xFF213422)
val DarkSecondaryContainer = Color(0xFF374B37)
val DarkOnSecondaryContainer = Color(0xFFD0E8CF)

val DarkTertiary = Color(0xFFE5C56E)
val DarkOnTertiary = Color(0xFF3F2E00)
val DarkTertiaryContainer = Color(0xFF5A4400)
val DarkOnTertiaryContainer = Color(0xFFFFF0C7)

val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

val DarkBackground = Color(0xFF1A110D)       // Marrón oscuro cálido
val DarkOnBackground = Color(0xFFF1DFD8)
val DarkSurface = Color(0xFF1A110D)          // Marrón oscuro cálido
val DarkOnSurface = Color(0xFFF1DFD8)
val DarkSurfaceVariant = Color(0xFF53433D)
val DarkOnSurfaceVariant = Color(0xFFD8C2B8)

val DarkOutline = Color(0xFFA08D84)
val DarkOutlineVariant = Color(0xFF53433D)

val DarkSurfaceTint = Color(0xFFFFB599)
val DarkSurfaceBright = Color(0xFF423632)
val DarkSurfaceDim = Color(0xFF1A110D)
val DarkSurfaceContainerLowest = Color(0xFF140C08)
val DarkSurfaceContainerLow = Color(0xFF231917)
val DarkSurfaceContainer = Color(0xFF271D1A)
val DarkSurfaceContainerHigh = Color(0xFF322724)
val DarkSurfaceContainerHighest = Color(0xFF3D322F)

// ============================================
// SEMANTIC COLORS (sin cambios - ya funcionan)
// ============================================

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
