# HogarFix — Contexto para Claude Code

> Lee este documento primero para entender el proyecto.

---

## Qué es HogarFix

App de **registro de mantenimiento del hogar**. Permite a los usuarios:
- Registrar intervenciones (reparaciones, mantenimientos)
- Gestionar inventario de electrodomésticos
- Guardar contactos de profesionales
- Configurar recordatorios de mantenimiento periódico

**Plataformas:** Android + iOS (MVP), Web (futuro)

---

## Stack Tecnológico

- **Lenguaje:** Kotlin
- **UI:** Compose Multiplatform + Material 3
- **Arquitectura:** MVVM + Clean Architecture
- **Persistencia:** Room Multiplatform (SQLite local)
- **DI:** Koin
- **Navegación:** Compose Navigation

Ver detalles completos en `doc/tech_stack.md`

---

## Estructura del Proyecto

```
hogarfix/
├── composeApp/src/
│   ├── commonMain/          # 90% del código (compartido)
│   │   └── kotlin/com/hogarfix/
│   │       ├── di/          # Módulos Koin
│   │       ├── data/        # Repositories, DAOs, Entities
│   │       ├── domain/      # UseCases, Models, Interfaces
│   │       └── ui/          # Screens, ViewModels, Components
│   ├── androidMain/         # Código específico Android
│   └── iosMain/             # Código específico iOS
├── iosApp/                  # Xcode wrapper
└── doc/                     # Documentación
```

---

## Documentación Clave

| Documento | Contenido |
|-----------|-----------|
| `doc/roadmap.md` | Fases de desarrollo y timeline |
| `doc/tech_stack.md` | Stack tecnológico y dependencias |
| `doc/data_model.md` | Entidades, relaciones, enums |
| `doc/ui_screens.md` | Pantallas y flujos de navegación |
| `doc/tasks/todo.md` | Tareas de la fase actual |

---

## Modelo de Datos (Resumen)

**Entidades principales:**
- `Intervention` — Registro de trabajo realizado
- `HomeItem` — Electrodoméstico o elemento del hogar
- `Professional` — Contacto de profesional
- `Reminder` — Recordatorio de mantenimiento

**Relaciones:**
- Intervention → Professional (N:1)
- Intervention → HomeItem (N:1)
- Reminder → HomeItem (N:1)

Ver detalles en `doc/data_model.md`

---

## Convenciones de Código

### Naming
- Pantallas: `XxxScreen` (ej: `InterventionListScreen`)
- ViewModels: `XxxViewModel`
- UseCases: `VerbNounUseCase` (ej: `GetInterventionsUseCase`)
- Entities DB: `XxxEntity`
- Models dominio: `Xxx`

### Arquitectura por capa
```
UI (Composables) → ViewModel → UseCase → Repository → DAO
```

---

## Estado Actual del Proyecto

**Fases completadas:**
- Fase 1 — Fundamentos
- Fase 2 — Intervenciones
- Fase 3 — Inventario del Hogar
- Fase 4 — Profesionales
- Fase 5 — Recordatorios de Mantenimiento

**Rediseño UI completado:** Nueva paleta terracota mediterránea + Dashboard funcional (ver `doc/redesigning-plan.md`)

**Próxima fase:** Fase 6 — Home + Diferenciadores UI + Localización España

**Tareas pendientes:** Ver `doc/tasks/todo.md`

---

## Diferenciadores Clave

1. **Offline-first sin cuenta** — Todo local con Room
2. **Dashboard de gastos** — Visualización de costes
3. **Plantillas por tipo vivienda** — Onboarding inteligente
4. **Localización España** — ITE, revisión gas, etc.
5. **Timeline visual fotos** — Antes/después emocional

---

## Proceso obligatorio tras cada funcionalidad nueva

Después de implementar cualquier funcionalidad nueva o modificación, se deben completar estos dos pasos antes de considerar la tarea terminada:

1. **Crear tests** para todas las funciones creadas o modificadas:
   - Tests de mappers (roundtrip entity↔domain, null handling, JSON)
   - Tests de use cases (CRUD completo con fake repositories)
   - Tests de modelos (propiedades computadas, validación)
   - Tests de helpers/lógica de negocio
   - Ejecutar `./gradlew :composeApp:testDebugUnitTest` y verificar que pasan todos

2. **Actualizar documentación** relevante:
   - `doc/tasks/todo.md` — Marcar tareas completadas, añadir nuevas
   - `doc/roadmap.md` — Actualizar estado de fases
   - `doc/CLAUDE.md` — Actualizar estado del proyecto
   - `doc/ui_screens.md` — Si hay pantallas o componentes nuevos
   - `doc/data_model.md` — Si hay cambios en entidades o relaciones
   - `doc/tech-stack.md` — Si hay nuevos expect/actual o dependencias

---

## Qué NO hacer

- No añadir backend/sync todavía (es feature futura)
- No crear Desktop target (eliminado del roadmap)
- No añadir anuncios nunca
- No sobre-ingeniería: mantener simple

---

## Comandos Útiles

```bash
# Compilar Android
./gradlew :composeApp:assembleDebug

# Compilar iOS (requiere Mac)
./gradlew :composeApp:iosSimulatorArm64Main

# Ejecutar tests
./gradlew :composeApp:testDebugUnitTest
```

---

## Preguntas Frecuentes

**¿Por qué Room y no SQLDelight?**
Room tiene mejor soporte multiplatform desde 2024 y es más familiar para devs Android.

**¿Por qué Koin y no Hilt?**
Koin es multiplatform, Hilt solo funciona en Android.

**¿Por qué no hay backend?**
MVP es offline-first. Backend vendrá con la versión Web para habilitar sync.