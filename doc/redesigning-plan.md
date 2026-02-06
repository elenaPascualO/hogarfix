# HogarFix — Plan de Rediseño UI ✅ COMPLETADO

> **Estado:** Implementado en Android (06/02/2026). iOS tiene problemas de compatibilidad de versiones pre-existentes.

> Inspirado en competidores americanos: Homellow, HomeLedger, HomeZada, Dwellin, HomeManager.
> Objetivo: pasar de un diseño genérico azul frío a una estética cálida terracota, moderna y "hogareña".
> Dashboard sencillo — no abrumar al usuario.

---

## Competidores Analizados

| App | Color Principal | Estilo | Lo que copiamos |
|-----|-----------------|--------|-----------------|
| **Homellow** | Naranja `#f97316` + crema | Cálido, acogedor | Tono cálido, fondos crema |
| **HomeLedger** | Teal `hsla(181,75%,29%)` | Profesional, fresco | Cards limpias, dashboard simple |
| **HomeZada** | Púrpura `#4a3651` | Sofisticado | Jerarquía visual clara |
| **Dwellin** | Neutros + oscuros | Minimalista, eco | Diseño limpio, no abrumador |
| **HomeManager** | Rosa `#C2246E` + Teal | Vibrante | Bottom nav con iconos descriptivos |

### Patrones comunes que adoptamos:
- Dashboard como pantalla principal con resumen
- Paleta cálida (no azul corporativo)
- Fondos crema (no blanco puro)
- Cards redondeadas con sombras suaves
- Iconos específicos por categoría
- Status badges con colores semáforo (verde/naranja/rojo)
- Bottom nav con 4-5 tabs + iconos descriptivos
- Quick actions (botones circulares)

### Lo que NO copiamos (diferenciadores HogarFix):
- Timeline de fotos antes/después (nuestro diferenciador)
- Offline-first sin cuentas
- Localización para España
- Sin AI assistant (mantenerlo simple)

---

## Fase A: Nueva Paleta de Colores

### Color.kt
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/theme/Color.kt`

Paleta terracota cálida (distinta de Homellow — más suave y mediterránea):

```
ANTES (azul frío)              →  DESPUÉS (terracota cálido)
─────────────────────────────────────────────────────────────
Primary:     #1565C0           →  #D4754E (terracota suave)
PrimaryCont: #D1E4FF           →  #FFDBCE (melocotón claro)
Secondary:   #535F70           →  #6B7B6A (verde salvia)
SecondaryCo: #D7E3F8           →  #D0E8CF (salvia claro)
Tertiary:    #6B5778           →  #7D6B3A (dorado cálido)
TertiaryCon: #F3DAFF           →  #FFF0C7 (dorado claro)
Background:  #FDFCFF           →  #FFF8F5 (crema cálido)
Surface:     #FDFCFF           →  #FFF8F5 (crema cálido)
SurfaceVar:  #DFE2EB           →  #F5DED3 (melocotón suave)
Outline:     #73777F           →  #85746B (marrón suave)
OutlineVar:  #C3C6CF           →  #D8C2B8 (beige)
```

Nuevos tokens de superficie Material 3:
```
SurfaceTint:              #D4754E
SurfaceBright:            #FFFBFF
SurfaceDim:               #E5D7D0
SurfaceContainerLowest:   #FFFFFF
SurfaceContainerLow:      #FFF1EB
SurfaceContainer:         #FBEBE3
SurfaceContainerHigh:     #F5E5DD
SurfaceContainerHighest:  #EFDFD7
```

**SIN CAMBIOS**: CategoryColors (13 colores), StatusColors, ReminderColors — ya funcionan bien.

Dark mode cálido:
```
Dark Primary:     #FFB599 (terracota claro)
Dark Background:  #1A110D (marrón oscuro cálido, NO gris frío)
Dark Surface:     #1A110D
```

### Theme.kt
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/theme/Theme.kt`

- Actualizar `LightColorScheme` y `DarkColorScheme` con nuevos colores
- Integrar `shapes = AppShapes` en `MaterialTheme`

### Shape.kt — NUEVO
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/theme/Shape.kt`

```kotlin
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // badges, chips pequeños
    small = RoundedCornerShape(8.dp),        // filter chips, mini cards
    medium = RoundedCornerShape(12.dp),      // cards estándar
    large = RoundedCornerShape(16.dp),       // diálogos, bottom sheets
    extraLarge = RoundedCornerShape(24.dp)   // FAB
)
```

---

## Fase B: Tipografía

### Type.kt
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/theme/Type.kt`

```
Display/Headline: FontWeight.SemiBold (era Normal) → títulos más impactantes
Body:             letterSpacing 0.15sp/0.1sp (era 0.5/0.25) → más compacto y moderno
Labels:           FontWeight.SemiBold (era Medium) → mejor legibilidad en chips
```

---

## Fase C: Rediseño de Componentes

### C1. CategorySelector.kt — Iconos específicos por categoría
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/components/CategorySelector.kt`

Reemplazar los iconos genéricos (Build/Home) con Material Icons Extended (ya en dependencias):

```
PLUMBING      → Icons.Outlined.Plumbing
ELECTRICAL    → Icons.Outlined.ElectricalServices
APPLIANCES    → Icons.Outlined.Kitchen
PAINTING      → Icons.Outlined.FormatPaint
LOCKSMITH     → Icons.Outlined.Lock
HVAC          → Icons.Outlined.AcUnit
CARPENTRY     → Icons.Outlined.Carpenter
GARDEN        → Icons.Outlined.Yard
CLEANING      → Icons.Outlined.CleaningServices
ROOFING       → Icons.Outlined.Roofing
FLOORING      → Icons.Outlined.Layers
WINDOWS_DOORS → Icons.Outlined.Window
OTHER         → Icons.Outlined.Handyman
```

- Hacer `getCategoryIcon()` **public** (se necesita en otros componentes)
- Añadir icono dentro del `CategoryChip` (icono + texto)

### C2. StatusChip.kt — Icono + color suave
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/components/StatusChip.kt`

```
ANTES: fondo sólido naranja/azul/verde + texto blanco
DESPUÉS: fondo pastel (15% alpha) + icono + texto en color

PENDING     → Schedule     + naranja suave
IN_PROGRESS → PlayCircle   + azul suave
COMPLETED   → CheckCircle  + verde suave
```

### C3. InterventionCard.kt — Rediseño
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/components/InterventionCard.kt`

```
ANTES                          DESPUÉS
──────────────────────────────────────────
Icono 48dp                  →  Icono 56dp
Sin accent bar              →  Barra lateral izq 4dp (color categoría)
Fecha solo texto            →  Icono calendario + fecha
Elevation 2dp               →  Elevation 1dp (más sutil)
Sin shape custom            →  Shape medium (12dp corners)
Surface color               →  SurfaceContainerLowest
```

### C4. EmptyStateView.kt — Tinte cálido
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/components/EmptyStateView.kt`

Icono: `primary.copy(alpha = 0.4f)` en lugar de gris.

---

## Fase D: Dashboard Sencillo

> Principio: NO abrumar al usuario. Solo 3 secciones claras.

### D1. HomeState.kt — NUEVO
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/screens/home/HomeState.kt`

```kotlin
data class HomeState(
    val monthlyExpense: Double = 0.0,
    val pendingCount: Int = 0,
    val recentInterventions: List<Intervention> = emptyList(), // máx 3
    val isLoading: Boolean = true
)
```

### D2. HomeViewModel.kt — NUEVO (mínimo)
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/screens/home/HomeViewModel.kt`

Usa `GetInterventionsUseCase` existente. Calcula:
- Gasto del mes actual
- Conteo de pendientes
- 3 intervenciones más recientes

### D3. AppModule.kt — Registrar HomeViewModel
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/di/AppModule.kt`

### D4. HomeScreen.kt — Dashboard limpio
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/screens/home/HomeScreen.kt`

```
┌─────────────────────────────────┐
│                                 │
│  ¡Hola!                        │
│  Resumen de tu hogar            │
│                                 │
│  ┌─────────────────────────┐    │
│  │ 💰 Gasto este mes       │    │
│  │    245€                 │    │
│  │    2 trabajos pendientes│    │
│  └─────────────────────────┘    │
│                                 │
│  Acciones rápidas               │
│  (●) Trabajo  (●) Inventario   │
│                (●) Recordatorio │
│                                 │
│  Actividad reciente  [Ver todos]│
│  ┌─────────────────────────┐    │
│  │ 🔧 Arreglo fuga         │    │
│  └─────────────────────────┘    │
│  ┌─────────────────────────┐    │
│  │ ⚡ Cambio enchufe        │    │
│  └─────────────────────────┘    │
│  ┌─────────────────────────┐    │
│  │ 🎨 Pintar habitación    │    │
│  └─────────────────────────┘    │
│                                 │
├─────────────────────────────────┤
│ 🏠  🔧  📦  👷  ⏰              │
└─────────────────────────────────┘
```

Solo 3 secciones: Saludo+Resumen, Acciones rápidas, Actividad reciente.

### D5. SummaryCard.kt — NUEVO componente
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/components/SummaryCard.kt`

Card con: icono + título + valor grande + subtítulo.

### D6. QuickActionButton.kt — NUEVO componente
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/components/QuickActionButton.kt`

Botón circular 56dp + icono + label debajo.

### D7. AppNavHost.kt — Navegación
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/navigation/AppNavHost.kt`

Pasar callbacks de navegación al HomeScreen: `onNavigateToInterventions`, `onNavigateToInventory`, `onNavigateToReminders`, `onNavigateToForm`.

---

## Fase E: Bottom Navigation

### App.kt — Iconos descriptivos + estilo cálido
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/App.kt`

```
ANTES                      →  DESPUÉS
──────────────────────────────────────
Home/Home                  →  Dashboard/Dashboard
Build/Build                →  Construction/Construction
List/List                  →  Inventory2/Inventory2
Person/Person              →  ContactPhone/ContactPhone
Notifications/Notific.     →  NotificationsActive/NotificationsActive
```

NavigationBar: `containerColor = surfaceContainerHigh`, `tonalElevation = 0.dp`

---

## Fase F: Polish Listas

### InterventionListScreen.kt — FilterChips con iconos
**Archivo**: `composeApp/src/commonMain/kotlin/com/hogarfix/ui/screens/interventions/InterventionListScreen.kt`

Añadir `leadingIcon` a los `FilterChip` usando `getCategoryIcon()` (ahora public).

---

## Orden de Implementación

```
 1. Color.kt              ← nueva paleta terracota
 2. Shape.kt              ← NUEVO archivo
 3. Theme.kt              ← esquemas + shapes
 4. Type.kt               ← tipografía mejorada
    ─── BUILD CHECK ───
 5. CategorySelector.kt   ← 13 iconos específicos
 6. StatusChip.kt         ← icono + pastel
 7. InterventionCard.kt   ← accent bar + icono grande
 8. EmptyStateView.kt     ← tinte cálido
    ─── BUILD CHECK ───
 9. SummaryCard.kt        ← NUEVO componente
10. QuickActionButton.kt  ← NUEVO componente
11. HomeState.kt          ← NUEVO
12. HomeViewModel.kt      ← NUEVO
13. AppModule.kt          ← registrar VM
14. HomeScreen.kt         ← dashboard sencillo
15. AppNavHost.kt         ← navegación
    ─── BUILD CHECK ───
16. App.kt                ← bottom nav icons
17. InterventionListScreen← polish filtros
    ─── BUILD CHECK FINAL ───
```

---

## Resumen de Archivos

### Crear (5 archivos nuevos)

| Archivo | Propósito |
|---------|-----------|
| `ui/theme/Shape.kt` | Sistema de bordes redondeados |
| `ui/components/QuickActionButton.kt` | Botones circulares dashboard |
| `ui/components/SummaryCard.kt` | Card resumen gastos |
| `ui/screens/home/HomeState.kt` | Estado del dashboard |
| `ui/screens/home/HomeViewModel.kt` | ViewModel del dashboard |

### Modificar (11 archivos existentes)

| Archivo | Cambios principales |
|---------|--------------------|
| `ui/theme/Color.kt` | Paleta terracota + crema + tokens superficie |
| `ui/theme/Theme.kt` | Esquemas light/dark + shapes |
| `ui/theme/Type.kt` | SemiBold headlines + spacing compacto |
| `ui/components/CategorySelector.kt` | 13 iconos específicos, getCategoryIcon público |
| `ui/components/StatusChip.kt` | Icono + fondo pastel 15% alpha |
| `ui/components/InterventionCard.kt` | Accent bar + icono 56dp + shape 12dp |
| `ui/components/EmptyStateView.kt` | Tinte cálido primary |
| `ui/screens/home/HomeScreen.kt` | Reescritura → dashboard 3 secciones |
| `ui/navigation/AppNavHost.kt` | Callbacks navegación HomeScreen |
| `App.kt` | Iconos nav descriptivos + estilo cálido |
| `di/AppModule.kt` | Registrar HomeViewModel |

*Todos los paths relativos a `composeApp/src/commonMain/kotlin/com/hogarfix/`*

---

## Verificación

| Checkpoint | Qué verificar |
|------------|---------------|
| Post Fase A+B | Build OK. Toda la app en tonos cálidos. Dark mode funciona. |
| Post Fase C | Cada categoría tiene icono propio. Cards rediseñadas. Status chips suaves. |
| Post Fase D | Dashboard muestra gasto mensual, acciones rápidas, 3 intervenciones recientes. |
| Post Fase E+F | Bottom nav con iconos descriptivos. Filtros con iconos. |
| Final | Test Android emulador + iOS Simulator. Light + dark mode. |
