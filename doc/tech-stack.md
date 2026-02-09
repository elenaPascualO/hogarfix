# HogarFix — Stack Tecnológico

> Kotlin + Compose Multiplatform (Android, iOS → Web)

---

## Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                         UI Layer                            │
│                   Compose Multiplatform                     │
│                      (Material 3)                           │
├─────────────────────────────────────────────────────────────┤
│                     Presentation Layer                      │
│                   ViewModels + UI State                     │
├─────────────────────────────────────────────────────────────┤
│                      Domain Layer                           │
│                UseCases + Domain Models                     │
├─────────────────────────────────────────────────────────────┤
│                       Data Layer                            │
│              Repositories + Data Sources                    │
├─────────────────────────────────────────────────────────────┤
│                     Persistence Layer                       │
│                 Room (SQLite local)                         │
└─────────────────────────────────────────────────────────────┘
```

**Patrón:** MVVM + Clean Architecture

---

## Stack por Capa

| Capa | Tecnología | Versión |
|------|------------|---------|
| **UI** | Compose Multiplatform | 1.8.0 |
| **Design System** | Material 3 | - |
| **Arquitectura** | MVVM + Clean Architecture | - |
| **Persistencia** | Room Multiplatform | 2.7.1 |
| **DI** | Koin Multiplatform | 4.0.4 |
| **Navegación** | Compose Navigation | 2.8.0-alpha10 |
| **Imágenes** | Coil 3 | 3.1.0 |
| **Fechas** | kotlinx-datetime | 0.6.2 |
| **Async** | kotlinx-coroutines | 1.10.1 |
| **Build** | Gradle + Version Catalogs | - |

---

## Plataformas Target

| Plataforma | Estado Framework | Prioridad |
|------------|------------------|-----------|
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
│       │
│       ├── androidMain/           # Código específico Android
│       │   └── kotlin/com/hogarfix/
│       │       ├── MainApplication.kt
│       │       └── platform/
│       │
│       ├── commonTest/             # Tests unitarios (kotlin.test)
│       │   └── kotlin/com/hogarfix/
│       │       ├── data/mapper/   # Tests de mappers
│       │       ├── domain/        # Tests de modelos y use cases
│       │       └── ui/            # Tests de helpers UI
│       │
│       ├── iosMain/               # Código específico iOS
│       │   └── kotlin/com/hogarfix/
│       │       └── platform/
│       │
│       └── wasmJsMain/            # Código específico Web (futuro)
│           └── kotlin/com/hogarfix/
│               └── platform/
│
├── iosApp/                        # Xcode project wrapper
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
└── settings.gradle.kts
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

[libraries]
# Room
room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }

# Koin
koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koin" }

# Coil (imágenes)
coil-compose = { module = "io.coil-kt.coil3:coil-compose", version.ref = "coil" }

# Kotlinx
kotlinx-datetime = { module = "org.jetbrains.kotlinx:kotlinx-datetime", version.ref = "kotlinx-datetime" }
kotlinx-coroutines = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "kotlinx-coroutines" }

# Navigation
navigation-compose = { module = "org.jetbrains.androidx.navigation:navigation-compose", version.ref = "navigation" }

[plugins]
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
compose-multiplatform = { id = "org.jetbrains.compose", version.ref = "compose-multiplatform" }
compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
android-application = { id = "com.android.application", version.ref = "agp" }
room = { id = "androidx.room", version.ref = "room" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

---

## expect/actual por Plataforma

Funcionalidades que requieren implementación específica:

| Funcionalidad | Android | iOS | Web (futuro) |
|---------------|---------|-----|--------------|
| **Notificaciones** | NotificationManager + WorkManager | UNUserNotificationCenter | Web Notifications API |
| **Cámara/Galería** | PickVisualMedia (ActivityResult) | PHPicker | `<input type="file">` |
| **Almacenamiento fotos** | App internal storage (filesDir) | App documents dir | IndexedDB |
| **Llamar teléfono** | Intent `tel:` | UIApplication.open | window.open("tel:") |
| **Abrir WhatsApp** | Intent wa.me | URL scheme | window.open |
| **Persistencia DB** | Room (SQLite) | Room (SQLite) | IndexedDB / Backend |
| **Fecha/Hora actual** | Clock.System (kotlinx-datetime) | NSDate (expect/actual) | Clock.System |
| **PlatformActions** | Intent ACTION_DIAL / wa.me | UIApplication.sharedApplication | window.open |
| **TimeUtils** | kotlinx-datetime | NSDateFormatter (expect/actual) | kotlinx-datetime |

---

## Convenciones de Código

### Naming

| Elemento | Convención | Ejemplo |
|----------|------------|---------|
| Pantalla | `XxxScreen` | `InterventionListScreen` |
| ViewModel | `XxxViewModel` | `InterventionListViewModel` |
| UseCase | `VerbNounUseCase` | `GetInterventionsUseCase` |
| Repository | `XxxRepository` | `InterventionRepository` |
| DAO | `XxxDao` | `InterventionDao` |
| Entity (DB) | `XxxEntity` | `InterventionEntity` |
| Model (Domain) | `Xxx` | `Intervention` |
| Componente UI | `XxxCard`, `XxxItem` | `InterventionCard` |

### State Management

```kotlin
// UI State
data class InterventionListState(
    val interventions: List<Intervention> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: Category? = null,
    val error: String? = null
)

// UI Events
sealed interface InterventionListEvent {
    data class Search(val query: String) : InterventionListEvent
    data class FilterByCategory(val category: Category?) : InterventionListEvent
    data class Delete(val id: Long) : InterventionListEvent
}
```

---

## Herramientas de Desarrollo

| Herramienta | Uso |
|-------------|-----|
| **Android Studio** (Ladybug+) | IDE principal |
| **Xcode** | Build iOS |
| **KMP Wizard** | Generar proyecto inicial |
| **klibs.io** | Buscar librerías KMP compatibles |

---

## Recursos

- [Kotlin Multiplatform Wizard](https://kmp.jetbrains.com/)
- [Compose Multiplatform Docs](https://www.jetbrains.com/compose-multiplatform/)
- [Room KMP Migration Guide](https://developer.android.com/kotlin/multiplatform/room)
- [Material 3 Design](https://m3.material.io/)
- [klibs.io](https://klibs.io/) — Catálogo de librerías KMP