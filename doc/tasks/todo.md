# HogarFix — Tareas Actuales

> **Fase actual:** Fase 5 — Recordatorios (COMPLETADA)

---

## Fase 1 — Fundamentos (COMPLETADA ✅)

**Objetivo:** Esqueleto tecnico funcional

### Configuracion del proyecto

- [x] Crear proyecto KMP con wizard de Android Studio
- [x] Configurar version catalog (`gradle/libs.versions.toml`)
- [x] Verificar que compila en Android
- [x] Verificar que compila en iOS (simulador)

### Tema y diseno

- [x] Crear tema Material 3
- [x] Definir colores por categoria

### Dependency Injection

- [x] Configurar Koin

### Base de datos

- [x] Configurar Room Multiplatform
- [x] Crear entidades
- [x] Crear TypeConverters
- [x] Crear DAOs con queries basicas

### Navegacion

- [x] Configurar Compose Navigation
- [x] Implementar Bottom Navigation
- [x] Crear pantallas placeholder

### Modelos de dominio

- [x] Crear enums
- [x] Crear modelos de dominio

---

## Fase 2 — Intervenciones (COMPLETADA ✅)

**Objetivo:** Funcionalidad core de registro de trabajos

### Domain Layer

- [x] `InterventionRepository` interface
- [x] `GetInterventionsUseCase`
- [x] `SaveInterventionUseCase`
- [x] `DeleteInterventionUseCase`

### Data Layer

- [x] `InterventionMapper` (Entity ↔ Domain)
- [x] `PhotoStorage` (expect/actual para Android e iOS)
- [x] `InterventionRepositoryImpl`

### UI Components

- [x] `CategorySelector` — Selector de categorias con chips y colores
- [x] `StatusChip` — Badge de estado con colores
- [x] `InterventionCard` — Tarjeta para listas
- [x] `PhotoGallery` — Galeria horizontal con Coil
- [x] `PhotoPicker` (expect/actual) — Selector de fotos
- [x] `DeleteConfirmationDialog` — Dialogo de confirmacion
- [x] `EmptyStateView` — Estado vacio con CTA

### Screens

- [x] `InterventionListScreen` con LazyColumn y filtros
- [x] `InterventionListViewModel` con State/Event pattern
- [x] `InterventionFormScreen` para crear/editar
- [x] `InterventionFormViewModel`

### Navigation & DI

- [x] Ruta `InterventionForm(id: Long?)` en NavRoutes
- [x] Navegacion conectada en AppNavHost
- [x] Dependencias registradas en AppModule (Koin)

### Utilities

- [x] `TimeUtils` (expect/actual) para manejo de fechas cross-platform

---

## Entregable de Fase 2

App que:
- [x] Compila en Android e iOS
- [x] Lista intervenciones con LazyColumn
- [x] Filtra por categoria
- [x] Crea nuevas intervenciones
- [x] Edita intervenciones existentes
- [x] Selecciona fotos de galeria
- [x] Cambia estado (pendiente/en curso/completado)
- [x] Muestra estado vacio cuando no hay datos

---

## Rediseño UI (COMPLETADO ✅)

Implementado el rediseño completo de la interfaz según `doc/redesigning-plan.md`:

- [x] Nueva paleta terracota mediterránea (Color.kt)
- [x] Sistema de shapes redondeados (Shape.kt - nuevo)
- [x] Tipografía SemiBold mejorada (Type.kt)
- [x] Dashboard con 3 secciones (HomeScreen, HomeViewModel, HomeState)
- [x] Componentes nuevos: SummaryCard, QuickActionButton
- [x] CategorySelector con 13 iconos específicos
- [x] StatusChip con icono + fondo pastel
- [x] InterventionCard con accent bar lateral
- [x] EmptyStateView con tinte cálido
- [x] Bottom nav con iconos descriptivos
- [x] FilterChips con iconos de categoría

**Nota:** Funciona en Android. iOS tiene problemas de compatibilidad de versiones pre-existentes (lifecycle + navigation + koin).

---

## Notas

- Requiere Java 17+ para compilar
- Los warnings de deprecation sobre `Instant` se pueden ignorar (kotlinx-datetime)
- Los warnings de expect/actual classes en Beta se pueden ignorar

---

## Fase 3 — Inventario del Hogar (COMPLETADA ✅)

**Objetivo:** Registro de electrodomésticos y elementos

### Domain Layer

- [x] `HomeItemRepository` interface
- [x] `GetHomeItemsUseCase`
- [x] `SaveHomeItemUseCase`
- [x] `DeleteHomeItemUseCase`

### Data Layer

- [x] `HomeItemMapper` (Entity ↔ Domain)
- [x] `HomeItemRepositoryImpl`
- [x] Refactorizar `PhotoStorage` para soportar múltiples tipos de entidad

### UI Components

- [x] `WarrantyBadge` — Badge con colores según días restantes de garantía
- [x] `HomeItemCard` — Tarjeta con marca, modelo, ubicación, intervenciones
- [x] `SwipeToDeleteContainer` — Componente swipe-to-delete reutilizable

### Screens

- [x] `InventoryListScreen` con LazyColumn y filtros por categoría
- [x] `HomeItemListViewModel` con State/Event pattern
- [x] `HomeItemFormScreen` para crear/editar (nombre, marca, modelo, categoría, fechas, ubicación, fotos, notas)
- [x] `HomeItemFormViewModel`

### Navigation & DI

- [x] Ruta `HomeItemForm(id: Long?)` en NavRoutes
- [x] Navegación conectada en AppNavHost
- [x] Dependencias registradas en AppModule (Koin)

### Funcionalidad de Borrado (Intervenciones + Inventario)

- [x] Swipe-to-delete en listas (InterventionListScreen, InventoryListScreen)
- [x] Botón eliminar en formularios de edición
- [x] Diálogo de confirmación antes de borrar

---

## Entregable de Fase 3

App que:
- [x] Lista elementos del hogar con LazyColumn
- [x] Filtra por categoría
- [x] Muestra badge de garantía con colores (verde/amarillo/rojo)
- [x] Crea nuevos elementos con todos los campos
- [x] Edita elementos existentes
- [x] Elimina elementos (swipe o botón en formulario)
- [x] Muestra contador de intervenciones por elemento
- [x] Selecciona fotos de galería

---

## Fase 4 — Profesionales (COMPLETADA ✅)

**Objetivo:** Agenda de contactos de oficios

### Domain Layer

- [x] `ProfessionalRepository` interface
- [x] `GetProfessionalsUseCase`
- [x] `SaveProfessionalUseCase`
- [x] `DeleteProfessionalUseCase`

### Data Layer

- [x] `ProfessionalMapper` (Entity ↔ Domain)
- [x] `ProfessionalRepositoryImpl`
- [x] Cambiar `Professional.specialty` de `String` a `Category`

### UI Components

- [x] `ProfessionalCard` — Tarjeta con categoria, rating, boton llamar
- [x] `RatingBar` — Componente estrellas 1-5 (display + interactivo)

### Screens

- [x] `ProfessionalListScreen` con LazyColumn y filtros por categoria
- [x] `ProfessionalListViewModel` con State/Event pattern
- [x] `ProfessionalFormScreen` para crear/editar (nombre, telefono, email, especialidad, valoracion, notas)
- [x] `ProfessionalFormViewModel`

### Navigation & DI

- [x] Ruta `ProfessionalForm(id: Long?)` en NavRoutes
- [x] Navegacion conectada en AppNavHost
- [x] Dependencias registradas en AppModule (Koin)

### Platform Actions

- [x] `PlatformActions.kt` (expect/actual) para abrir dialer
- [x] Implementacion Android (Intent ACTION_DIAL)
- [x] Implementacion iOS (UIApplication openURL)

---

## Entregable de Fase 4

App que:
- [x] Lista profesionales con LazyColumn
- [x] Filtra por categoria (especialidad)
- [x] Crea nuevos profesionales con todos los campos
- [x] Edita profesionales existentes
- [x] Elimina profesionales (swipe y desde formulario)
- [x] Muestra valoracion con estrellas
- [x] Muestra contador de intervenciones por profesional
- [x] Boton para llamar directamente desde la tarjeta

---

## Fase 5 — Recordatorios de Mantenimiento (COMPLETADA ✅)

**Objetivo:** Sistema de recordatorios con recurrencia e integracion en dashboard

### Data Layer

- [x] `ReminderMapper` (Entity ↔ Domain)
- [x] `ReminderRepository` interface
- [x] `ReminderRepositoryImpl` con complete() que recalcula nextDueDate

### Domain Layer

- [x] `GetRemindersUseCase` (getAllActive, getOverdue, getUpcoming, getByHomeItem, getById)
- [x] `SaveReminderUseCase`
- [x] `DeleteReminderUseCase`
- [x] `CompleteReminderUseCase`

### UI Components

- [x] `ReminderCard` con urgency dot (rojo/amarillo/verde), boton completar
- [x] `CreateReminderDialog` con picker intervalo (dias/semanas/meses/anos)

### Screens

- [x] `ReminderListScreen` con 3 secciones (Vencidos/Proximos/Programados)
- [x] `ReminderListViewModel` con State/Event pattern
- [x] `ReminderFormScreen` para crear/editar (titulo, descripcion, categoria, intervalo, fecha, activo)
- [x] `ReminderFormViewModel` con decomposeInterval()

### Integracion

- [x] Dialog "Programar recordatorio?" tras guardar nueva intervencion
- [x] Dashboard: alerta roja de vencidos + seccion proximos mantenimientos
- [x] Selector de profesional en formulario de intervencion (dropdown cuando DoneBy=PROFESSIONAL)

### Navigation & DI

- [x] Ruta `ReminderForm(id: Long?)` en NavRoutes
- [x] Navegacion conectada en AppNavHost
- [x] Dependencias registradas en AppModule (Koin)

---

## Entregable de Fase 5

App que:
- [x] Lista recordatorios en 3 secciones con colores de urgencia
- [x] Filtra por categoria
- [x] Crea recordatorios con intervalo configurable (dias/semanas/meses/anos)
- [x] Edita recordatorios existentes
- [x] Elimina recordatorios (swipe y desde formulario)
- [x] Completa recordatorios y recalcula proxima fecha
- [x] Pregunta al usuario si quiere programar recordatorio tras crear intervencion
- [x] Muestra alertas de vencidos y proximos en el dashboard
- [x] Permite vincular un profesional a una intervencion

---

## Siguiente: Fase 6 — Home + Diferenciadores UI + Localizacion Espana

Tareas pendientes:

- [ ] Notificaciones locales (expect/actual)
- [ ] Plantillas recordatorios Espana (ITE, revision gas, caldera, etc.)
- [ ] Onboarding con selector tipo vivienda
- [ ] Timeline visual fotos antes/despues
- [ ] Busqueda global por texto
- [ ] Selector de HomeItem vinculado en formulario de intervencion

**Entregable:** App lista para testing con todos los diferenciadores core
