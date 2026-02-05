# HogarFix — Plan de Desarrollo

> App de registro de mantenimiento del hogar
> Stack: Kotlin + Compose Multiplatform (Android, iOS, Desktop, Web)
> Estrategia: Mobile-first → Desktop → Web

---

## 1. Visión del Producto

Una app sencilla para registrar todo lo que ocurre en tu hogar: reparaciones, mantenimientos, profesionales que te han atendido, garantías de electrodomésticos y recordatorios de tareas periódicas. Como el cuaderno de toda la vida, pero siempre en tu bolsillo.

### Plataformas objetivo

| Plataforma | Estado del framework | Prioridad | Fase |
|---|---|---|---|
| **Android** | ✅ Stable | 🥇 Lanzamiento | Fases 1-7 |
| **iOS** | ✅ Stable | 🥇 Lanzamiento | Fases 1-7 |
| **Desktop** (Windows, macOS, Linux) | ✅ Stable | 🥈 Post-lanzamiento | Fase 8 |
| **Web** (Kotlin/Wasm) | 🧪 Beta | 🥉 Futuro | Fase 9 |

**Estrategia mobile-first:** El 90%+ del código vive en `commonMain` y es compartido. Desktop y Web se añaden después como targets adicionales sin reescribir código, solo implementando las piezas `expect/actual` específicas de cada plataforma.

---

## 2. Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| **UI** | Compose Multiplatform (Material 3) |
| **Arquitectura** | MVVM + Clean Architecture |
| **Persistencia** | Room Multiplatform (o SQLDelight) |
| **DI** | Koin Multiplatform |
| **Navegación** | Compose Navigation (multiplatform) |
| **Imágenes** | Coil 3 (multiplatform) |
| **Fechas** | kotlinx-datetime |
| **Notificaciones** | expect/actual con APIs nativas |
| **Build** | Gradle (version catalogs) |
| **IDE** | Android Studio (Ladybug+) o IntelliJ IDEA Ultimate |

---

## 3. Estructura del Proyecto

```
hogarfix/
├── composeApp/                    # Código compartido + UI
│   └── src/
│       ├── commonMain/            # Todo el código compartido (~90%)
│       │   ├── kotlin/com/hogarfix/
│       │   │   ├── App.kt                  # Entry point Compose
│       │   │   ├── di/                     # Módulos Koin
│       │   │   ├── data/
│       │   │   │   ├── local/
│       │   │   │   │   ├── AppDatabase.kt  # Room/SQLDelight DB
│       │   │   │   │   ├── dao/            # DAOs
│       │   │   │   │   └── entity/         # Entidades DB
│       │   │   │   ├── repository/         # Implementaciones
│       │   │   │   └── mapper/             # Entity <-> Domain
│       │   │   ├── domain/
│       │   │   │   ├── model/              # Modelos de dominio
│       │   │   │   ├── repository/         # Interfaces
│       │   │   │   └── usecase/            # Casos de uso
│       │   │   └── ui/
│       │   │       ├── theme/              # Colores, tipografía, tema
│       │   │       ├── components/         # Componentes reutilizables
│       │   │       ├── navigation/         # NavHost, rutas
│       │   │       ├── home/               # Pantalla principal
│       │   │       ├── intervention/       # CRUD intervenciones
│       │   │       ├── inventory/          # Inventario del hogar
│       │   │       ├── reminders/          # Recordatorios
│       │   │       ├── contacts/           # Agenda profesionales
│       │   │       └── search/             # Búsqueda y filtros
│       │   └── resources/                  # Strings, imágenes, iconos
│       ├── androidMain/           # Código específico Android
│       │   └── kotlin/com/hogarfix/
│       │       ├── MainApplication.kt
│       │       └── platform/              # expect/actual Android
│       ├── iosMain/               # Código específico iOS
│       │   └── kotlin/com/hogarfix/
│       │       └── platform/              # expect/actual iOS
│       ├── desktopMain/           # Código específico Desktop (Fase 8)
│       │   └── kotlin/com/hogarfix/
│       │       ├── Main.kt                # Entry point JVM (ventana)
│       │       └── platform/              # expect/actual Desktop
│       └── wasmJsMain/            # Código específico Web (Fase 9)
│           └── kotlin/com/hogarfix/
│               ├── Main.kt                # Entry point Web
│               └── platform/              # expect/actual Web
├── iosApp/                        # Xcode project wrapper
│   └── iosApp/
│       └── iOSApp.swift
├── gradle/
│   └── libs.versions.toml         # Version catalog
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 4. Modelo de Datos

### 4.1 Entidades principales

```
┌─────────────────────┐     ┌─────────────────────┐
│    Intervention      │     │    HomeItem          │
├─────────────────────┤     ├─────────────────────┤
│ id: Long (PK)       │     │ id: Long (PK)        │
│ title: String        │     │ name: String          │
│ description: String? │     │ brand: String?        │
│ date: LocalDate      │     │ model: String?        │
│ category: Category   │     │ category: Category    │
│ laborCost: Double?   │     │ purchaseDate: LocalDate? │
│ materialCost: Double?│     │ warrantyEndDate: LocalDate? │
│ status: Status       │     │ location: String?     │
│ doneBy: DoneBy       │     │ notes: String?        │
│ professionalId: Long?│     │ photoUris: List<String> │
│ homeItemId: Long?    │     │ createdAt: Instant    │
│ photoUris: List<String>│   │ updatedAt: Instant    │
│ notes: String?       │     └─────────┬───────────┘
│ createdAt: Instant   │               │
│ updatedAt: Instant   │               │ 1:N
└────────┬────────────┘               │
         │                    ┌───────┴───────────┐
         │ N:1                │    Reminder        │
         │                    ├───────────────────┤
┌────────┴────────────┐      │ id: Long (PK)      │
│   Professional       │      │ title: String       │
├─────────────────────┤      │ description: String? │
│ id: Long (PK)        │      │ intervalDays: Int    │
│ name: String         │      │ nextDueDate: LocalDate│
│ phone: String?       │      │ homeItemId: Long?    │
│ email: String?       │      │ category: Category   │
│ specialty: String    │      │ isActive: Boolean    │
│ personalRating: Int? │      │ lastCompletedDate: LocalDate? │
│ notes: String?       │      │ createdAt: Instant   │
│ createdAt: Instant   │      └───────────────────┘
└─────────────────────┘
```

### 4.2 Enums

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

enum class Status {
    PENDING,         // Pendiente
    IN_PROGRESS,     // En curso
    COMPLETED        // Terminado
}

enum class DoneBy {
    MYSELF,          // Yo mismo
    PROFESSIONAL     // Profesional
}
```

---

## 5. Pantallas y Navegación

```
                    ┌──────────────┐
                    │  Splash /    │
                    │  Onboarding  │
                    └──────┬───────┘
                           │
                    ┌──────▼───────┐
              ┌─────┤    Home      ├─────┐
              │     │  (Timeline)  │     │
              │     └──────┬───────┘     │
              │            │             │
     ┌────────▼──┐  ┌──────▼─────┐  ┌───▼─────────┐
     │ Inventario │  │ Intervenc. │  │ Profesionales│
     │ del Hogar  │  │   Lista    │  │   Agenda     │
     └────────┬──┘  └──────┬─────┘  └───┬─────────┘
              │            │             │
     ┌────────▼──┐  ┌──────▼─────┐  ┌───▼─────────┐
     │  Detalle / │  │ Crear/Edit │  │  Detalle /   │
     │  Editar    │  │ Intervenc. │  │  Editar      │
     └───────────┘  └────────────┘  └──────────────┘

     Bottom Navigation:
     [🏠 Inicio] [🔧 Intervenciones] [📦 Inventario] [👷 Contactos] [⏰ Recordatorios]
```

### 5.1 Detalle de pantallas

**Home (Timeline)**
- Lista cronológica de intervenciones recientes
- Tarjetas con: título, categoría (icono + color), fecha, coste, estado
- FAB para añadir nueva intervención
- Resumen rápido: gasto del mes, intervenciones pendientes, próximos recordatorios

**Crear/Editar Intervención**
- Formulario con todos los campos del modelo
- Selector de categoría con iconos
- Toggle "Lo hice yo" / "Profesional" (si profesional, selector de contacto)
- Selector de elemento del hogar vinculado (opcional)
- Captura de fotos (cámara o galería)
- Botones guardar / cancelar

**Inventario del Hogar**
- Grid o lista de electrodomésticos y elementos
- Filtro por categoría
- Badge de alerta en items con garantía próxima a vencer
- Detalle con historial de intervenciones vinculadas

**Agenda de Profesionales**
- Lista con nombre, especialidad, valoración (estrellas)
- Detalle con historial de trabajos realizados
- Click to call / click to WhatsApp

**Recordatorios**
- Lista de mantenimientos programados
- Indicador visual: al día (verde), próximo (amarillo), vencido (rojo)
- Activar/desactivar recordatorios individualmente
- Crear recordatorio vinculado a elemento del hogar

**Búsqueda y Filtros (accesible desde cualquier lista)**
- Búsqueda por texto libre
- Filtros: categoría, rango de fechas, rango de coste, estado, profesional

---

## 6. Fases de Desarrollo

### Fase 1 — Fundamentos (Semana 1-2)

- [ ] Crear proyecto KMP con wizard de Android Studio
- [ ] Configurar dependencias (version catalog)
- [ ] Implementar tema Material 3 (colores, tipografía)
- [ ] Configurar Koin (módulos DI)
- [ ] Configurar Room/SQLDelight (base de datos, entidades, DAOs)
- [ ] Implementar navegación base (Bottom Nav + NavHost)
- [ ] Pantallas placeholder para cada sección

### Fase 2 — Intervenciones (Semana 3-4)

- [ ] Pantalla lista de intervenciones (LazyColumn con tarjetas)
- [ ] Pantalla crear/editar intervención (formulario completo)
- [ ] Selector de categorías con iconos y colores
- [ ] Captura de fotos (expect/actual para cámara y galería)
- [ ] Repository + UseCase para CRUD intervenciones
- [ ] Estado: pendiente / en curso / terminado
- [ ] Eliminar intervención con confirmación

### Fase 3 — Inventario del Hogar (Semana 5)

- [ ] Pantalla lista de elementos del hogar
- [ ] Pantalla crear/editar elemento
- [ ] Campos: nombre, marca, modelo, fecha compra, garantía, foto
- [ ] Alerta visual de garantías próximas a vencer
- [ ] Vincular intervenciones a elementos del hogar

### Fase 4 — Profesionales (Semana 6)

- [ ] Pantalla agenda de profesionales
- [ ] Pantalla crear/editar profesional
- [ ] Valoración personal (1-5 estrellas)
- [ ] Vincular profesional a intervenciones
- [ ] Historial de trabajos por profesional
- [ ] Acciones: llamar, WhatsApp (intents nativos)

### Fase 5 — Recordatorios (Semana 7)

- [ ] Pantalla lista de recordatorios
- [ ] Crear recordatorio: título, intervalo, elemento vinculado
- [ ] Notificaciones locales (expect/actual)
- [ ] Lógica de recurrencia (cada X días/meses)
- [ ] Marcar como completado → recalcular próxima fecha
- [ ] Indicadores visuales por estado (verde/amarillo/rojo)

### Fase 6 — Home + Búsqueda + Pulido (Semana 8)

- [ ] Pantalla Home con timeline y resumen
- [ ] Búsqueda global por texto
- [ ] Filtros combinados (categoría, fecha, coste, estado)
- [ ] Animaciones y transiciones
- [ ] Onboarding (primera apertura)
- [ ] Empty states con ilustraciones
- [ ] Revisión de UX y accesibilidad

### Fase 7 — Testing y Lanzamiento Mobile (Semana 9-10)

- [ ] Tests unitarios (UseCases, Repositories)
- [ ] Tests de UI (Compose test)
- [ ] Testing manual en dispositivos Android + iOS
- [ ] Configurar firma de app (Android) y provisioning (iOS)
- [ ] Preparar assets para stores (capturas, descripción, icono)
- [ ] Publicar en Google Play (beta cerrada)
- [ ] Publicar en App Store (TestFlight)

### Fase 8 — Desktop: Windows, macOS, Linux (Semana 11-12)

> El framework Desktop es estable. Se reutiliza todo `commonMain`.

- [ ] Añadir target `jvm` al `build.gradle.kts`
- [ ] Crear `desktopMain/` con entry point (`main()` + `Window {}`)
- [ ] Implementar `expect/actual` para Desktop:
  - Notificaciones → Notificaciones del sistema (tray notifications)
  - Almacenamiento de fotos → Sistema de archivos local
  - Cámara → Diálogo de selección de archivo (no hay cámara en desktop)
  - Llamar/WhatsApp → Abrir enlaces `tel:` / `https://wa.me/` en navegador
- [ ] Adaptar UI para pantallas grandes:
  - Navegación lateral (NavigationRail) en vez de Bottom Navigation
  - Layouts más anchos con master-detail en pantallas grandes
  - Atajos de teclado (Ctrl+N nuevo, Ctrl+F buscar, etc.)
- [ ] Menú de ventana nativo (Archivo, Editar, Ayuda)
- [ ] Soporte para redimensionar ventana (responsive)
- [ ] Empaquetado con Conveyor o `jpackage`:
  - `.msi` / `.exe` para Windows
  - `.dmg` para macOS
  - `.deb` / `.AppImage` para Linux
- [ ] Testing en los 3 sistemas operativos

### Fase 9 — Web (Futuro, cuando Kotlin/Wasm sea Stable)

> Actualmente en Beta. Planificar pero no ejecutar hasta estabilización.

- [ ] Añadir target `wasmJs` al `build.gradle.kts`
- [ ] Crear `wasmJsMain/` con entry point web
- [ ] Implementar `expect/actual` para Web:
  - Notificaciones → Web Notifications API / Service Workers
  - Almacenamiento de fotos → IndexedDB o almacenamiento en servidor
  - Cámara → `<input type="file" capture="camera">`
  - Persistencia → IndexedDB (o migrar a backend con API)
- [ ] Decisión arquitectónica: local-only vs backend sync
  - Si se añade backend: Ktor server + API REST + base de datos remota
  - Esto habilitaría también sync entre dispositivos (funcionalidad avanzada)
- [ ] Adaptar UI para navegador:
  - Navegación lateral tipo sidebar
  - Responsive para móvil y escritorio
- [ ] Deploy: hosting estático (GitHub Pages, Netlify, Vercel)
- [ ] PWA (Progressive Web App) para instalación desde navegador

---

### Resumen de expect/actual por plataforma

Estas son las piezas que necesitan implementación específica por plataforma.
Todo lo demás (UI, lógica, base de datos, navegación) vive en `commonMain`.

| Funcionalidad | Android | iOS | Desktop | Web |
|---|---|---|---|---|
| **Notificaciones** | NotificationManager + WorkManager | UNUserNotificationCenter | Tray notifications | Web Notifications API |
| **Cámara/Galería** | Intent + ActivityResult | PHPicker + UIImagePicker | FileDialog (solo galería) | `<input file>` |
| **Almacenamiento fotos** | App internal storage | App documents dir | Sistema de archivos | IndexedDB |
| **Llamar teléfono** | Intent `tel:` | `UIApplication.open` | Abrir en navegador | `window.open("tel:")` |
| **Abrir WhatsApp** | Intent `wa.me` | URL scheme | Abrir en navegador | `window.open` |
| **Persistencia DB** | Room (SQLite) | Room (SQLite) | Room (SQLite JVM) | IndexedDB / Backend |
| **Navegación layout** | Bottom Navigation | Bottom Navigation | NavigationRail / Sidebar | Sidebar responsive |

---

## 7. Patrones y Convenciones

### Arquitectura por capa

```
UI (Composables) → ViewModel → UseCase → Repository → DataSource (Room)
```

### Naming conventions

| Elemento | Convención | Ejemplo |
|----------|-----------|---------|
| Pantalla | `XxxScreen` | `InterventionListScreen` |
| ViewModel | `XxxViewModel` | `InterventionListViewModel` |
| UseCase | `VerbNounUseCase` | `GetInterventionsUseCase` |
| Repository | `XxxRepository` / `XxxRepositoryImpl` | `InterventionRepository` |
| DAO | `XxxDao` | `InterventionDao` |
| Entity (DB) | `XxxEntity` | `InterventionEntity` |
| Model (Domain) | `Xxx` | `Intervention` |
| Composable componente | `XxxCard`, `XxxItem` | `InterventionCard` |

### State management

```kotlin
// UI State por pantalla
data class InterventionListState(
    val interventions: List<Intervention> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: Category? = null,
    val error: String? = null
)

// Eventos de UI
sealed interface InterventionListEvent {
    data class Search(val query: String) : InterventionListEvent
    data class FilterByCategory(val category: Category?) : InterventionListEvent
    data class Delete(val id: Long) : InterventionListEvent
}
```

---

## 8. Dependencias (libs.versions.toml)

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
ksp = { id = "com.google.devtools.ksp", version = "2.1.10-1.0.31" }
```

---

## 9. Monetización (futuro)

| Modelo | Descripción |
|--------|------------|
| **Freemium** | Gratis con 1 vivienda y funcionalidades básicas |
| **Pro (pago único o suscripción)** | Multi-vivienda, exportar PDF/Excel, backup en nube, dashboard de gastos, OCR de facturas |
| **Sin anuncios** | La app nunca tendrá anuncios intrusivos para mantener la experiencia limpia |

---

## 10. Nombre y Branding

Sugerencias de nombre (verificar disponibilidad en stores):

- **HogarFix** — Directo, fácil de recordar
- **CasaLog** — Evoca "log" como registro
- **MiCasa** — Sencillo y cercano
- **Mantenio** — Juego con "mantenimiento"
- **FixLog** — Corto, internacional

---

## 11. Recursos Útiles

- [Kotlin Multiplatform Wizard](https://kmp.jetbrains.com/) — Genera el proyecto inicial
- [Compose Multiplatform Docs](https://www.jetbrains.com/compose-multiplatform/)
- [klibs.io](https://klibs.io/) — Catálogo de librerías KMP
- [Material 3 Design](https://m3.material.io/) — Guía de diseño
- [Room KMP Migration Guide](https://developer.android.com/kotlin/multiplatform/room)
