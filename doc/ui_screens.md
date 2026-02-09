# HogarFix — Pantallas y Navegación

---

## Mapa de Navegación

```
                      ┌──────────────┐
                      │   Splash     │
                      └──────┬───────┘
                             │
                      ┌──────▼───────┐
                      │  Onboarding  │ (solo primera vez)
                      │  - Tipo de   │
                      │    vivienda  │
                      └──────┬───────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
              ▼              ▼              ▼
┌─────────────────────────────────────────────────────────┐
│                    Bottom Navigation                     │
├───────────┬───────────┬───────────┬───────────┬─────────┤
│  🏠 Home  │ 🔧 Inter- │ 📦 Inven- │ 👷 Profe- │ ⏰ Recor-│
│           │ venciones │ tario     │ sionales  │ datorios│
└─────┬─────┴─────┬─────┴─────┬─────┴─────┬─────┴────┬────┘
      │           │           │           │          │
      ▼           ▼           ▼           ▼          ▼
   Home       Lista       Lista       Lista      Lista
   Screen     Interv.     Items       Profes.    Remind.
      │           │           │           │          │
      │     ┌─────┴─────┐     │     ┌─────┴────┐     │
      │     ▼           ▼     ▼     ▼          ▼     ▼
      │   Crear      Detalle  │   Crear     Detalle  │
      │   Interv.    Interv.  │   Prof.     Prof.    │
      │                       │                      │
      │                 ┌─────┴─────┐          ┌─────┴─────┐
      │                 ▼           ▼          ▼           ▼
      │              Crear       Detalle    Crear       Detalle
      │              Item        Item       Remind.     Remind.
      │
      └──────> Búsqueda global (accesible desde cualquier lista)
```

---

## Pantallas Detalladas

### 1. Splash Screen

**Ruta:** `/splash`

- Logo de HogarFix
- Transición automática a Home (o Onboarding si es primera vez)
- Duración: ~1.5 segundos

---

### 2. Onboarding Screen

**Ruta:** `/onboarding`

**Solo se muestra la primera vez que se abre la app.**

```
┌─────────────────────────────────┐
│                                 │
│     🏠 Bienvenido a HogarFix    │
│                                 │
│   ¿Qué tipo de vivienda tienes? │
│                                 │
│   ┌─────────────────────────┐   │
│   │ 🏢 Piso en ciudad       │   │
│   └─────────────────────────┘   │
│   ┌─────────────────────────┐   │
│   │ 🏡 Casa con jardín      │   │
│   └─────────────────────────┘   │
│   ┌─────────────────────────┐   │
│   │ 🏊 Chalet con piscina   │   │
│   └─────────────────────────┘   │
│   ┌─────────────────────────┐   │
│   │ ⚙️ Configurar después   │   │
│   └─────────────────────────┘   │
│                                 │
└─────────────────────────────────┘
```

**Acción:** Al seleccionar tipo, se precargan recordatorios típicos.

---

### 3. Home Screen (✅ IMPLEMENTADO)

**Ruta:** `/home`

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
│ 📊  🔧  📦  👤  🔔              │
└─────────────────────────────────┘
```

**Componentes implementados:**
- `SummaryCard` — Resumen de gastos del mes + pendientes
- `QuickActionButton` — Botones circulares para acceso rápido
- `InterventionCard` — 3 intervenciones más recientes
- Navegación a otras secciones

**Pendientes para futuro:**
- Timeline visual con fotos antes/después
- Alertas de recordatorios próximos/vencidos
- FAB para crear nueva intervención
- Barra de búsqueda

---

### 4. Lista de Intervenciones

**Ruta:** `/interventions`

```
┌─────────────────────────────────┐
│ Intervenciones              🔍  │
├─────────────────────────────────┤
│ [Todas ▼] [Filtros]             │
├─────────────────────────────────┤
│                                 │
│ ┌─────────────────────────────┐ │
│ │ 🔧 Arreglo fuga cocina      │ │
│ │ Fontanería · 15 Ene 2025    │ │
│ │ ✅ Completado    120€       │ │
│ └─────────────────────────────┘ │
│                                 │
│ ┌─────────────────────────────┐ │
│ │ ⚡ Cambio enchufe salón     │ │
│ │ Electricidad · 10 Ene 2025  │ │
│ │ ✅ Completado    0€ (yo)    │ │
│ └─────────────────────────────┘ │
│                                 │
│ ┌─────────────────────────────┐ │
│ │ 🎨 Pintar habitación        │ │
│ │ Pintura · 5 Ene 2025        │ │
│ │ 🟡 En curso       80€       │ │
│ └─────────────────────────────┘ │
│                                 │
│                           [+]   │
├─────────────────────────────────┤
│ 🏠  🔧  📦  👷  ⏰              │
└─────────────────────────────────┘
```

**Funcionalidades:**
- Lista con tarjetas
- Filtros: categoría, estado, fecha, coste
- Ordenación: fecha (desc por defecto)
- FAB para crear nueva
- Click → Detalle

---

### 5. Crear/Editar Intervención

**Ruta:** `/interventions/new` o `/interventions/{id}/edit`

```
┌─────────────────────────────────┐
│ ← Nueva intervención            │
├─────────────────────────────────┤
│                                 │
│  Título *                       │
│  ┌─────────────────────────┐    │
│  │ Arreglo fuga cocina     │    │
│  └─────────────────────────┘    │
│                                 │
│  Categoría *                    │
│  [🔧 Fontanería         ▼]      │
│                                 │
│  Fecha *                        │
│  [📅 15/01/2025            ]    │
│                                 │
│  ¿Quién lo hizo? *              │
│  (•) Yo mismo  ( ) Profesional  │
│                                 │
│  Profesional (si aplica)        │
│  [👷 Seleccionar...        ▼]   │
│                                 │
│  Elemento vinculado             │
│  [📦 Fregadero cocina      ▼]   │
│                                 │
│  Coste mano de obra             │
│  ┌─────────────────────────┐    │
│  │ 80                    € │    │
│  └─────────────────────────┘    │
│                                 │
│  Coste materiales               │
│  ┌─────────────────────────┐    │
│  │ 40                    € │    │
│  └─────────────────────────┘    │
│                                 │
│  Estado                         │
│  [✅ Completado            ▼]   │
│                                 │
│  Fotos                          │
│  [📷 Añadir] [🖼️ Galería]       │
│  ┌─────┐ ┌─────┐                │
│  │ 📸  │ │ 📸  │                │
│  └─────┘ └─────┘                │
│                                 │
│  Notas                          │
│  ┌─────────────────────────┐    │
│  │ Cambiar junta cada 2... │    │
│  └─────────────────────────┘    │
│                                 │
│  [ Cancelar ]    [ Guardar ]    │
│                                 │
└─────────────────────────────────┘
```

---

### 6. Lista de Inventario (✅ IMPLEMENTADO)

**Ruta:** `/inventory`

```
┌─────────────────────────────────┐
│ Inventario                      │
├─────────────────────────────────┤
│ [Todos] [Fontanería] [Elec...] │
├─────────────────────────────────┤
│                                 │
│ ┌─────────────────────────────┐ │
│ │▌ [Icon] Lavadora    [30 d] │ │
│ │▌        Samsung · WW90      │ │
│ │▌        📍 Cocina           │ │
│ │▌        🔧 2 intervenciones │ │
│ └─────────────────────────────┘ │
│                                 │
│ ┌─────────────────────────────┐ │
│ │▌ [Icon] Caldera    [Vencida]│ │
│ │▌        Junkers · ZWR24     │ │
│ │▌        📍 Trastero         │ │
│ │▌        🔧 1 intervencion   │ │
│ └─────────────────────────────┘ │
│                                 │
│                           [+]   │
├─────────────────────────────────┤
│ 🏠  🔧  📦  👷  ⏰              │
└─────────────────────────────────┘
```

**Funcionalidades implementadas:**
- Lista con `HomeItemCard` y accent bar de color categoría
- Filtros por categoría (chips horizontales)
- `WarrantyBadge` con colores: verde (>90 días), amarillo (31-90), rojo (<30 o vencida)
- Contador de intervenciones asociadas
- Swipe-to-delete para eliminar
- FAB para crear nuevo elemento
- Estado vacío con CTA

**Badge de alerta** para garantías:
- Verde: Más de 90 días
- Amarillo: 31-90 días
- Rojo: Menos de 30 días o vencida

---

### 7. Lista de Profesionales (✅ IMPLEMENTADO)

**Ruta:** `/professionals`

```
┌─────────────────────────────────┐
│ Profesionales                    │
├─────────────────────────────────┤
│ [Todos] [Fontanería] [Elec...] │
├─────────────────────────────────┤
│                                 │
│ ┌─────────────────────────────┐ │
│ │▌ [Icon] Juan Pérez      📞 │ │
│ │▌        Fontanería          │ │
│ │▌        ⭐⭐⭐⭐⭐              │ │
│ │▌        🔧 3 intervenciones │ │
│ └─────────────────────────────┘ │
│                                 │
│ ┌─────────────────────────────┐ │
│ │▌ [Icon] Electricidad SL 📞 │ │
│ │▌        Electricidad        │ │
│ │▌        ⭐⭐⭐⭐               │ │
│ │▌        🔧 1 intervencion   │ │
│ └─────────────────────────────┘ │
│                                 │
│                           [+]   │
├─────────────────────────────────┤
│ 🏠  🔧  📦  👷  ⏰              │
└─────────────────────────────────┘
```

**Funcionalidades implementadas:**
- Lista con `ProfessionalCard` y accent bar de color categoria
- Filtros por categoria/especialidad (chips horizontales)
- `RatingBar` con estrellas 1-5
- Contador de intervenciones asociadas
- Swipe-to-delete para eliminar
- Boton llamar (icono telefono azul) con intent nativo
- FAB para crear nuevo profesional
- Estado vacio con CTA

---

### 8. Lista de Recordatorios

**Ruta:** `/reminders`

```
┌─────────────────────────────────┐
│ Recordatorios               🔍  │
├─────────────────────────────────┤
│                                 │
│ 🔴 Vencidos                     │
│ ┌─────────────────────────────┐ │
│ │ ⏰ Revisión caldera         │ │
│ │ Climatización · Anual       │ │
│ │ Vencido hace 5 días         │ │
│ │           [✓ Completar]     │ │
│ └─────────────────────────────┘ │
│                                 │
│ 🟡 Próximos (7 días)            │
│ ┌─────────────────────────────┐ │
│ │ ⏰ Limpiar filtros A/C      │ │
│ │ Climatización · Trimestral  │ │
│ │ En 3 días                   │ │
│ │           [✓ Completar]     │ │
│ └─────────────────────────────┘ │
│                                 │
│ 🟢 Programados                  │
│ ┌─────────────────────────────┐ │
│ │ ⏰ Revisión gas             │ │
│ │ Climatización · Cada 5 años │ │
│ │ En 8 meses                  │ │
│ └─────────────────────────────┘ │
│                                 │
│                           [+]   │
├─────────────────────────────────┤
│ 🏠  🔧  📦  👷  ⏰              │
└─────────────────────────────────┘
```

**Indicadores visuales:**
- 🔴 Rojo: Vencido
- 🟡 Amarillo: Próximo (< 7 días)
- 🟢 Verde: Al día

**Acción "Completar":** Marca como hecho y recalcula próxima fecha.

---

### 9. Búsqueda Global

**Ruta:** Modal/overlay desde cualquier pantalla

```
┌─────────────────────────────────┐
│ 🔍 Buscar...                  ✕ │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │ fuga                        │ │
│ └─────────────────────────────┘ │
│                                 │
│ Filtros                         │
│ Categoría: [Todas        ▼]    │
│ Desde:     [             📅]    │
│ Hasta:     [             📅]    │
│ Coste:     [0€] ────○─── [500€] │
│ Estado:    [Todos        ▼]    │
│                                 │
│ ─────────────────────────────── │
│                                 │
│ Resultados (2)                  │
│                                 │
│ ┌─────────────────────────────┐ │
│ │ 🔧 Arreglo fuga cocina      │ │
│ │ 15 Ene · Fontanería · 120€  │ │
│ └─────────────────────────────┘ │
│ ┌─────────────────────────────┐ │
│ │ 🔧 Fuga grifo baño          │ │
│ │ 3 Dic · Fontanería · 45€    │ │
│ └─────────────────────────────┘ │
│                                 │
└─────────────────────────────────┘
```

---

## Componentes Reutilizables

| Componente | Uso | Estado |
|------------|-----|--------|
| `InterventionCard` | Tarjeta de intervención en listas | ✅ |
| `HomeItemCard` | Tarjeta de elemento del hogar | ✅ |
| `ProfessionalCard` | Tarjeta de profesional | ✅ |
| `RatingBar` | Estrellas 1-5 (display + interactivo) | ✅ |
| `ReminderCard` | Tarjeta de recordatorio | ⬜ |
| `CategorySelector` | Selector de categorías con chips | ✅ |
| `CategoryIcon` | Icono con color de categoría | ✅ |
| `StatusChip` | Badge de estado con icono + color | ✅ |
| `WarrantyBadge` | Badge de garantía con colores | ✅ |
| `SummaryCard` | Tarjeta resumen gastos en Home | ✅ |
| `QuickActionButton` | Botón circular acceso rápido | ✅ |
| `PhotoGallery` | Galería horizontal de fotos | ✅ |
| `PhotoPicker` | Selector de fotos (expect/actual) | ✅ |
| `EmptyStateView` | Estado vacío con CTA | ✅ |
| `DeleteConfirmationDialog` | Diálogo confirmar borrado | ✅ |
| `SwipeToDeleteContainer` | Contenedor swipe-to-delete | ✅ |
| `SearchBar` | Barra de búsqueda | ⬜ |
| `FilterSheet` | Bottom sheet con filtros | ⬜ |

---

## Iconos por Categoría

| Categoría | Icono | Color |
|-----------|-------|-------|
| Fontanería | 🔧 | Azul |
| Electricidad | ⚡ | Amarillo |
| Electrodomésticos | 📺 | Gris |
| Pintura | 🎨 | Rosa |
| Cerrajería | 🔑 | Marrón |
| Climatización | ❄️ | Celeste |
| Carpintería | 🪚 | Madera |
| Jardín | 🌱 | Verde |
| Limpieza | 🧹 | Lila |
| Tejado | 🏠 | Rojo |
| Suelos | 🟫 | Marrón claro |
| Ventanas/Puertas | 🚪 | Gris oscuro |
| Otros | ⚙️ | Gris |

---

## Estados de UI

### Loading State
Skeleton loaders en listas mientras carga.

### Empty State
Ilustración + mensaje + CTA cuando no hay datos.

```
┌─────────────────────────────────┐
│                                 │
│           🔧                    │
│                                 │
│   No hay intervenciones aún     │
│                                 │
│   Registra tu primer trabajo    │
│   para empezar a llevar el      │
│   control de tu hogar.          │
│                                 │
│      [ + Crear primera ]        │
│                                 │
└─────────────────────────────────┘
```

### Error State
Mensaje de error + botón reintentar.