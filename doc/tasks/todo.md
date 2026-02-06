# HogarFix — Tareas Actuales

> **Fase actual:** Fase 3 — Inventario (COMPLETADA)

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

## Siguiente: Fase 4 — Profesionales

Tareas de la Fase 4:

- [ ] Pantalla lista de profesionales
- [ ] Pantalla crear/editar profesional
- [ ] Campos: nombre, teléfono, email, categoría, valoración
- [ ] Acciones: llamar, WhatsApp (intents nativos)
- [ ] Vincular profesional a intervenciones
- [ ] Historial de trabajos por profesional
- [ ] Repository + UseCase para CRUD

**Entregable:** Usuario tiene agenda de profesionales con historial de trabajos
