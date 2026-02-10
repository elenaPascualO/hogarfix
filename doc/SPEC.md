# HogarFix — Especificación Técnica

> App de registro de mantenimiento del hogar.
> Stack: Kotlin + Compose Multiplatform (Android, iOS → Web)

---

## Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                         UI Layer                                │
│                   Compose Multiplatform                         │
│                      (Material 3)                               │
├─────────────────────────────────────────────────────────────────┤
│                     Presentation Layer                          │
│                   ViewModels + UI State                         │
├─────────────────────────────────────────────────────────────────┤
│                      Domain Layer                               │
│                UseCases + Domain Models                         │
├─────────────────────────────────────────────────────────────────┤
│                       Data Layer                                │
│              Repositories + Data Sources                        │
├─────────────────────────────────────────────────────────────────┤
│                     Persistence Layer                           │
│                 Room (SQLite local)                             │
└─────────────────────────────────────────────────────────────────┘
```

**Patrón:** MVVM + Clean Architecture

---

## Stack Tecnológico

| Capa | Tecnología | Versión |
|------|------------|---------|
| **Lenguaje** | Kotlin | 2.1.10 |
| **UI** | Compose Multiplatform | 1.8.0 |
| **Design System** | Material 3 | - |
| **Persistencia** | Room Multiplatform | 2.7.1 |
| **DI** | Koin Multiplatform | 4.0.4 |
| **Navegación** | Compose Navigation | 2.8.0-alpha10 |
| **Imágenes** | Coil 3 | 3.1.0 |
| **Fechas** | kotlinx-datetime | 0.6.2 |
| **Async** | kotlinx-coroutines | 1.10.1 |
| **Build** | Gradle + Version Catalogs | - |

### Plataformas Target

| Plataforma | Estado | Prioridad |
|------------|--------|-----------|
| **Android** | Stable | MVP |
| **iOS** | Stable | MVP |
| **Web** (Kotlin/Wasm) | Beta | Futuro |

---

## Estructura del Proyecto

```
hogarfix/
├── composeApp/
│   └── src/
│       ├── commonMain/            # ~90% del código (compartido)
│       │   └── kotlin/com/hogarfix/
│       │       ├── App.kt
│       │       ├── di/            # Módulos Koin
│       │       ├── data/
│       │       │   ├── local/
│       │       │   │   ├── AppDatabase.kt
│       │       │   │   ├── dao/
│       │       │   │   └── entity/
│       │       │   ├── repository/
│       │       │   └── mapper/
│       │       ├── domain/
│       │       │   ├── model/
│       │       │   ├── repository/
│       │       │   └── usecase/
│       │       └── ui/
│       │           ├── theme/
│       │           ├── components/
│       │           ├── navigation/
│       │           └── screens/
│       ├── androidMain/           # Código específico Android
│       ├── commonTest/            # Tests unitarios (kotlin.test)
│       ├── iosMain/               # Código específico iOS
│       └── wasmJsMain/            # Código específico Web (futuro)
├── iosApp/                        # Xcode project wrapper
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Modelo de Datos

### Diagrama de Entidades

```
┌─────────────────────┐       ┌─────────────────────┐
│    Intervention     │       │      HomeItem       │
├─────────────────────┤       ├─────────────────────┤
│ id: Long (PK)       │       │ id: Long (PK)       │
│ title: String       │       │ name: String        │
│ description: String?│       │ brand: String?      │
│ date: LocalDate     │       │ model: String?      │
│ category: Category  │       │ category: Category  │
│ laborCost: Double?  │       │ purchaseDate: LocalDate? │
│ materialCost: Double?│      │ warrantyEndDate: LocalDate? │
│ status: Status      │       │ location: String?   │
│ doneBy: DoneBy      │       │ notes: String?      │
│ professionalId: Long?│──┐   │ photoUris: List<String> │
│ homeItemId: Long?───│──│───>│ createdAt: Instant  │
│ photoUris: List<String>│ │  │ updatedAt: Instant  │
│ notes: String?      │  │    └──────────┬──────────┘
│ createdAt: Instant  │  │               │
│ updatedAt: Instant  │  │               │ 1:N
└─────────────────────┘  │               │
                         │    ┌──────────▼──────────┐
┌─────────────────────┐  │    │      Reminder       │
│    Professional     │  │    ├─────────────────────┤
├─────────────────────┤  │    │ id: Long (PK)       │
│ id: Long (PK)       │<─┘    │ title: String       │
│ name: String        │       │ intervalDays: Int   │
│ phone: String?      │       │ nextDueDate: LocalDate │
│ email: String?      │       │ homeItemId: Long?   │
│ specialty: Category │       │ category: Category  │
│ personalRating: Int?│       │ isActive: Boolean   │
│ notes: String?      │       │ lastCompletedDate: LocalDate? │
│ createdAt: Instant  │       │ createdAt: Instant  │
└─────────────────────┘       └─────────────────────┘
```

### Entidades

#### Intervention
Registro de un trabajo realizado en el hogar.

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Primary key |
| title | String | Sí | Título descriptivo |
| description | String? | No | Descripción detallada |
| date | LocalDate | Sí | Fecha del trabajo |
| category | Category | Sí | Categoría del trabajo |
| laborCost | Double? | No | Coste de mano de obra |
| materialCost | Double? | No | Coste de materiales |
| status | Status | Sí | Estado actual |
| doneBy | DoneBy | Sí | Quién lo hizo |
| professionalId | Long? | No | FK a Professional |
| homeItemId | Long? | No | FK a HomeItem |
| photoUris | List\<String\> | No | URIs de fotos adjuntas |
| notes | String? | No | Notas adicionales |
| createdAt | Instant | Auto | Fecha de creación |
| updatedAt | Instant | Auto | Última modificación |

#### HomeItem
Electrodoméstico o elemento del hogar.

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Primary key |
| name | String | Sí | Nombre del elemento |
| brand | String? | No | Marca |
| model | String? | No | Modelo |
| category | Category | Sí | Categoría |
| purchaseDate | LocalDate? | No | Fecha de compra |
| warrantyEndDate | LocalDate? | No | Fin de garantía |
| location | String? | No | Ubicación en casa |
| notes | String? | No | Notas adicionales |
| photoUris | List\<String\> | No | URIs de fotos |
| createdAt | Instant | Auto | Fecha de creación |
| updatedAt | Instant | Auto | Última modificación |

#### Professional
Contacto de profesional o empresa.

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Primary key |
| name | String | Sí | Nombre o empresa |
| phone | String? | No | Teléfono |
| email | String? | No | Email |
| specialty | Category | Sí | Especialidad principal |
| personalRating | Int? | No | Valoración 1-5 |
| notes | String? | No | Notas adicionales |
| createdAt | Instant | Auto | Fecha de creación |

#### Reminder
Recordatorio de mantenimiento periódico.

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Primary key |
| title | String | Sí | Título del recordatorio |
| description | String? | No | Descripción |
| intervalDays | Int | Sí | Intervalo en días |
| nextDueDate | LocalDate | Sí | Próxima fecha |
| homeItemId | Long? | No | FK a HomeItem |
| category | Category | Sí | Categoría |
| isActive | Boolean | Sí | Si está activo |
| lastCompletedDate | LocalDate? | No | Última vez completado |
| createdAt | Instant | Auto | Fecha de creación |

### Enums

```kotlin
enum class Category {
    PLUMBING,        // Fontanería
    ELECTRICAL,      // Electricidad
    APPLIANCES,      // Electrodomésticos
    PAINTING,        // Pintura
    LOCKSMITH,       // Cerrajería
    HVAC,            // Climatización
    CARPENTRY,       // Carpintería
    GARDEN,          // Jardín
    CLEANING,        // Limpieza
    ROOFING,         // Tejado
    FLOORING,        // Suelos
    WINDOWS_DOORS,   // Ventanas y puertas
    OTHER            // Otros
}

enum class Status { PENDING, IN_PROGRESS, COMPLETED }
enum class DoneBy { MYSELF, PROFESSIONAL }
```

### Relaciones

| Relación | Tipo | Descripción |
|----------|------|-------------|
| Intervention → Professional | N:1 | Una intervención puede tener un profesional asignado |
| Intervention → HomeItem | N:1 | Una intervención puede estar vinculada a un elemento |
| Reminder → HomeItem | N:1 | Un recordatorio puede estar vinculado a un elemento |

---

## Pantallas y Navegación

### Mapa de Navegación

```
Splash → Onboarding (primera vez) → Bottom Navigation (5 tabs)

Tabs: Home | Intervenciones | Inventario | Profesionales | Recordatorios

Cada tab: Lista → Crear/Editar → Detalle
```

### Pantallas implementadas

| Pantalla | Ruta | Estado |
|----------|------|--------|
| Home Screen | `/home` | ✅ |
| Intervention List | `/interventions` | ✅ |
| Intervention Form | `/interventions/new` `/interventions/{id}/edit` | ✅ |
| Inventory List | `/inventory` | ✅ |
| HomeItem Form | `/inventory/new` `/inventory/{id}/edit` | ✅ |
| Professional List | `/professionals` | ✅ |
| Professional Form | `/professionals/new` `/professionals/{id}/edit` | ✅ |
| Reminder List | `/reminders` | ✅ |
| Reminder Form | `/reminders/new` `/reminders/{id}/edit` | ✅ |
| Onboarding | `/onboarding` | ⬜ |
| Búsqueda global | Modal/overlay | ⬜ |

### Componentes reutilizables

| Componente | Uso | Estado |
|------------|-----|--------|
| `InterventionCard` | Tarjeta de intervención en listas | ✅ |
| `HomeItemCard` | Tarjeta de elemento del hogar | ✅ |
| `ProfessionalCard` | Tarjeta de profesional | ✅ |
| `ReminderCard` | Tarjeta de recordatorio con urgency dot | ✅ |
| `RatingBar` | Estrellas 1-5 | ✅ |
| `CategorySelector` | Selector de categorías con chips | ✅ |
| `StatusChip` | Badge de estado con icono + color | ✅ |
| `WarrantyBadge` | Badge de garantía (verde/amarillo/rojo) | ✅ |
| `SummaryCard` | Resumen gastos en Home | ✅ |
| `QuickActionButton` | Botón circular acceso rápido | ✅ |
| `PhotoGallery` | Galería horizontal de fotos | ✅ |
| `PhotoPicker` | Selector de fotos (expect/actual) | ✅ |
| `EmptyStateView` | Estado vacío con CTA | ✅ |
| `DeleteConfirmationDialog` | Diálogo confirmar borrado | ✅ |
| `SwipeToDeleteContainer` | Contenedor swipe-to-delete | ✅ |
| `CreateReminderDialog` | Dialog post-intervención | ✅ |

---

## Diseño Visual / UI

> Estética cálida terracota mediterránea. Implementado en Android (febrero 2026).

### Paleta de Colores

| Token | Light | Dark | Descripción |
|-------|-------|------|-------------|
| Primary | `#D4754E` | `#FFB599` | Terracota suave |
| PrimaryContainer | `#FFDBCE` | `#8B4A2D` | Melocotón claro |
| Secondary | `#6B7B6A` | `#B4CBB3` | Verde salvia |
| Tertiary | `#7D6B3A` | `#E5D09B` | Dorado cálido |
| Background | `#FFF8F5` | `#1A110D` | Crema cálido / marrón oscuro |
| Surface | `#FFF8F5` | `#1A110D` | Crema cálido / marrón oscuro |
| SurfaceVariant | `#F5DED3` | `#4F3F38` | Melocotón suave |
| Outline | `#85746B` | `#A09088` | Marrón suave |

Tokens de superficie Material 3: `SurfaceTint (#D4754E)`, `SurfaceDim (#E5D7D0)`, `SurfaceContainer (#FBEBE3)`, `SurfaceContainerHigh (#F5E5DD)`.

Los colores de categorías (13), estados y recordatorios se mantienen del diseño original.

### Tipografía

| Estilo | Peso | Nota |
|--------|------|------|
| Display / Headline | `SemiBold` | Títulos más impactantes |
| Body | Normal, `letterSpacing 0.15sp` | Más compacto y moderno |
| Labels | `SemiBold` | Mejor legibilidad en chips |

### Sistema de Formas

```kotlin
extraSmall = 4.dp    // badges, chips pequeños
small = 8.dp         // filter chips, mini cards
medium = 12.dp       // cards estándar
large = 16.dp        // diálogos, bottom sheets
extraLarge = 24.dp   // FAB
```

### Iconos por Categoría

Cada categoría tiene un icono Material Icons específico:

| Categoría | Icono |
|-----------|-------|
| Fontanería | `Plumbing` |
| Electricidad | `ElectricalServices` |
| Electrodomésticos | `Kitchen` |
| Pintura | `FormatPaint` |
| Cerrajería | `Lock` |
| Climatización | `AcUnit` |
| Carpintería | `Carpenter` |
| Jardín | `Yard` |
| Limpieza | `CleaningServices` |
| Tejado | `Roofing` |
| Suelos | `Layers` |
| Ventanas y puertas | `Window` |
| Otros | `Handyman` |

### Componentes Clave del Diseño

| Componente | Diseño |
|------------|--------|
| **StatusChip** | Fondo pastel (15% alpha) + icono + texto en color del estado |
| **InterventionCard** | Barra lateral izquierda 4dp (color categoría), icono 56dp, elevation 1dp, shape 12dp |
| **EmptyStateView** | Tinte cálido `primary.copy(alpha = 0.4f)` |
| **Bottom Navigation** | `surfaceContainerHigh`, iconos descriptivos (Dashboard, Construction, Inventory2, ContactPhone, NotificationsActive) |

### Layout del Dashboard (HomeScreen)

Tres secciones:
1. **Saludo + Resumen**: gasto mensual, trabajos pendientes
2. **Acciones rápidas**: botones circulares (56dp) — Nueva intervención, Inventario, Recordatorio
3. **Actividad reciente**: últimas 3 intervenciones con enlace "Ver todos"

---

## expect/actual por Plataforma

| Funcionalidad | Android | iOS | Web (futuro) |
|---------------|---------|-----|--------------|
| **Cámara/Galería** | PickVisualMedia | PHPicker | `<input type="file">` |
| **Almacenamiento fotos** | filesDir | App documents dir | IndexedDB |
| **Llamar teléfono** | Intent `tel:` | UIApplication.open | window.open("tel:") |
| **Abrir WhatsApp** | Intent wa.me | URL scheme | window.open |
| **Persistencia DB** | Room (SQLite) | Room (SQLite) | IndexedDB / Backend |
| **Fecha/Hora** | Clock.System | NSDate (expect/actual) | Clock.System |
| **PlatformActions** | Intent ACTION_DIAL | UIApplication | window.open |
| **TimeUtils** | kotlinx-datetime | NSDateFormatter | kotlinx-datetime |
| **Notificaciones** | NotificationManager + WorkManager | UNUserNotificationCenter | Web Notifications API |

---

## Queries Comunes

```kotlin
// Intervenciones recientes
@Query("SELECT * FROM interventions ORDER BY date DESC LIMIT :limit")
fun getRecentInterventions(limit: Int): Flow<List<InterventionEntity>>

// Gastos por categoría
@Query("""
    SELECT category, SUM(laborCost + materialCost) as total
    FROM interventions
    WHERE date BETWEEN :startDate AND :endDate
    GROUP BY category
""")
fun getExpensesByCategory(startDate: LocalDate, endDate: LocalDate): Flow<List<CategoryExpense>>

// Recordatorios vencidos
@Query("SELECT * FROM reminders WHERE isActive = 1 AND nextDueDate <= :today")
fun getOverdueReminders(today: LocalDate): Flow<List<ReminderEntity>>

// Garantías próximas a vencer
@Query("""
    SELECT * FROM home_items
    WHERE warrantyEndDate IS NOT NULL
    AND warrantyEndDate BETWEEN :today AND :futureDate
""")
fun getItemsWithExpiringWarranty(today: LocalDate, futureDate: LocalDate): Flow<List<HomeItemEntity>>
```

---

## Notas de Implementación

### Almacenamiento de fotos
- Las fotos se guardan en el almacenamiento interno de la app
- En la DB solo se guarda la URI/path como String
- Se usa TypeConverter para `List<String>` ↔ JSON

### Fechas
- Usar `kotlinx-datetime` para `LocalDate` e `Instant`
- TypeConverters para Room: `LocalDate` ↔ `Long` (epoch days)

### State Management
```kotlin
// UI State
data class XxxListState(
    val items: List<Xxx> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: Category? = null,
    val error: String? = null
)

// UI Events
sealed interface XxxListEvent {
    data class Search(val query: String) : XxxListEvent
    data class FilterByCategory(val category: Category?) : XxxListEvent
    data class Delete(val id: Long) : XxxListEvent
}
```

---

## Dependencias (libs.versions.toml)

```toml
[versions]
kotlin = "2.1.10"
compose-multiplatform = "1.8.0"
agp = "8.7.3"
room = "2.7.1"
koin = "4.0.4"
coil = "3.1.0"
kotlinx-datetime = "0.6.2"
kotlinx-coroutines = "1.10.1"
navigation = "2.8.0-alpha10"
ksp = "2.1.10-1.0.31"
```

---

## Recursos

- [Kotlin Multiplatform Wizard](https://kmp.jetbrains.com/)
- [Compose Multiplatform Docs](https://www.jetbrains.com/compose-multiplatform/)
- [Room KMP Migration Guide](https://developer.android.com/kotlin/multiplatform/room)
- [Material 3 Design](https://m3.material.io/)
- [klibs.io](https://klibs.io/)
