# HogarFix — Tareas Actuales

> **Fase actual:** Fase 1 — Fundamentos (COMPLETADA)

---

## Fase 1 — Fundamentos (Semana 1-2)

**Objetivo:** Esqueleto tecnico funcional

### Configuracion del proyecto

- [x] Crear proyecto KMP con wizard de Android Studio
  - Targets: Android + iOS
  - Compose Multiplatform habilitado

- [x] Configurar version catalog (`gradle/libs.versions.toml`)
  - Room, Koin, Coil, Navigation, kotlinx-datetime, kotlinx-coroutines, KSP

- [x] Verificar que compila en Android
  - `./gradlew :composeApp:assembleDebug`

- [x] Verificar que compila en iOS (simulador)
  - `./gradlew :composeApp:compileKotlinIosSimulatorArm64`

### Tema y diseno

- [x] Crear tema Material 3
  - `ui/theme/Color.kt` — Paleta de colores
  - `ui/theme/Type.kt` — Tipografia
  - `ui/theme/Theme.kt` — HogarFixTheme composable

- [x] Definir colores por categoria
  - CategoryColors, StatusColors, ReminderColors

### Dependency Injection

- [x] Configurar Koin
  - `di/AppModule.kt` — Modulos vacios preparados
  - Inicializar en `MainApplication.kt` (Android)
  - Inicializar en `MainViewController.kt` (iOS)

### Base de datos

- [x] Configurar Room Multiplatform
  - `data/local/AppDatabase.kt` con @ConstructedBy para iOS
  - `data/local/DatabaseFactory.kt` (expect/actual)
  - Plugin KSP configurado en build.gradle

- [x] Crear entidades
  - `data/local/entity/InterventionEntity.kt`
  - `data/local/entity/HomeItemEntity.kt`
  - `data/local/entity/ProfessionalEntity.kt`
  - `data/local/entity/ReminderEntity.kt`

- [x] Crear TypeConverters
  - `data/local/Converters.kt`
  - `List<String>` <-> JSON (para photoUris)

- [x] Crear DAOs con queries basicas
  - `data/local/dao/InterventionDao.kt`
  - `data/local/dao/HomeItemDao.kt`
  - `data/local/dao/ProfessionalDao.kt`
  - `data/local/dao/ReminderDao.kt`

### Navegacion

- [x] Configurar Compose Navigation
  - `ui/navigation/NavRoutes.kt` — Rutas serializables
  - `ui/navigation/AppNavHost.kt` — NavHost principal

- [x] Implementar Bottom Navigation
  - 5 tabs: Inicio, Trabajos, Inventario, Contactos, Avisos
  - Material Icons integrados

- [x] Crear pantallas placeholder
  - `ui/screens/home/HomeScreen.kt`
  - `ui/screens/interventions/InterventionListScreen.kt`
  - `ui/screens/inventory/InventoryListScreen.kt`
  - `ui/screens/professionals/ProfessionalListScreen.kt`
  - `ui/screens/reminders/ReminderListScreen.kt`

### Modelos de dominio

- [x] Crear enums
  - `domain/model/Category.kt` (13 categorias)
  - `domain/model/Status.kt` (PENDING, IN_PROGRESS, COMPLETED)
  - `domain/model/DoneBy.kt` (MYSELF, PROFESSIONAL)

- [x] Crear modelos de dominio
  - `domain/model/Intervention.kt`
  - `domain/model/HomeItem.kt`
  - `domain/model/Professional.kt`
  - `domain/model/Reminder.kt`

---

## Entregable de Fase 1

App que:
- [x] Compila en Android e iOS
- [x] Muestra Bottom Navigation con 5 tabs
- [x] Navega entre pantallas placeholder
- [x] Tiene tema Material 3 aplicado
- [x] Tiene Room configurado (aunque sin datos)
- [x] Tiene Koin configurado

---

## Notas

- Requiere Java 17+ para compilar (usar OpenJDK via Homebrew)
- Los warnings de deprecation sobre `Instant` se pueden ignorar por ahora

---

## Siguiente: Fase 2 — Intervenciones

Ahora que la Fase 1 esta completada, las tareas de la Fase 2 seran:

- [ ] Pantalla lista de intervenciones (LazyColumn)
- [ ] Pantalla crear/editar intervencion
- [ ] Selector de categorias con iconos y colores
- [ ] Captura de fotos (expect/actual camara/galeria)
- [ ] Repository + UseCase para CRUD
- [ ] Estado: pendiente / en curso / terminado
- [ ] Eliminar con confirmacion
