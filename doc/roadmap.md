# HogarFix — Roadmap Unificado

> App de registro de mantenimiento del hogar
> Stack: Kotlin + Compose Multiplatform (Android + iOS → Web)

---

## Visión

**App de registro de mantenimiento del hogar** que se diferencia por:
- Offline-first sin necesidad de cuenta
- Localización profunda para el mercado español
- Simplicidad y experiencia emocional (timeline visual)

---

## Roadmap General

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  MVP MOBILE (Android + iOS)                                                 │
│  Fases 1-7 · Incluye diferenciadores core                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Fase 1        Fase 2          Fase 3         Fase 4         Fase 5        │
│  Fundamentos   Intervenciones  Inventario     Profesionales  Recordatorios │
│  ──────────>   ──────────────> ────────────>  ────────────>  ────────────> │
│  S1-S2         S3-S4           S5             S6             S7            │
│                                                              + Plantillas  │
│                                                                España      │
│                                                                             │
│  Fase 6                    Fase 7                                           │
│  Home + Dashboard gastos   Testing + Lanzamiento                            │
│  + Timeline visual fotos   ────────────────────>                            │
│  + Onboarding plantillas                                                    │
│  ──────────────────────>                                                    │
│  S8                        S9-S10                                           │
│                                                                             │
├─────────────────────────────────────────────────────────────────────────────┤
│  POST-LANZAMIENTO                                                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Fase 8                    Fase 9                                           │
│  Compartir historial       Web (cuando Wasm stable)                         │
│  (PDF/link)                ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─>                         │
│  ────────────────────>                                                      │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Fase 1 — Fundamentos (Semana 1-2) ✅ COMPLETADA

**Objetivo:** Esqueleto técnico funcional

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Crear proyecto KMP con wizard de Android Studio | Alta | ✅ |
| Configurar dependencias (version catalog) | Alta | ✅ |
| Implementar tema Material 3 | Alta | ✅ |
| Configurar Koin (módulos DI) | Alta | ✅ |
| Configurar Room (DB, entidades, DAOs) | Alta | ✅ |
| Implementar navegación base (Bottom Nav + NavHost) | Alta | ✅ |
| Pantallas placeholder para cada sección | Media | ✅ |

**Diferenciador incluido:** ✅ Offline-first sin cuenta (viene por arquitectura Room)

**Entregable:** App compilando en Android e iOS con navegación básica

---

## Fase 2 — Intervenciones (Semana 3-4) ✅ COMPLETADA

**Objetivo:** Funcionalidad core de registro de trabajos

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Pantalla lista de intervenciones (LazyColumn) | Alta | ✅ |
| Pantalla crear/editar intervención | Alta | ✅ |
| Selector de categorías con iconos y colores | Alta | ✅ |
| Captura de fotos (expect/actual cámara/galería) | Alta | ✅ |
| Repository + UseCase para CRUD | Alta | ✅ |
| Estado: pendiente / en curso / terminado | Media | ✅ |
| Eliminar con confirmación | Media | ✅ |

**Entregable:** Usuario puede crear, ver, editar y eliminar intervenciones con fotos

---

## Fase 3 — Inventario del Hogar (Semana 5) ✅ COMPLETADA

**Objetivo:** Registro de electrodomésticos y elementos

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Pantalla lista de elementos del hogar | Alta | ✅ |
| Pantalla crear/editar elemento | Alta | ✅ |
| Campos: nombre, marca, modelo, fecha compra, garantía, foto | Alta | ✅ |
| Alerta visual de garantías próximas a vencer | Media | ✅ |
| Vincular intervenciones a elementos | Media | ✅ |
| Swipe-to-delete + botón eliminar en formularios | Media | ✅ |

**Entregable:** Usuario puede gestionar inventario y ver qué elementos tienen trabajos asociados

---

## Fase 4 — Profesionales (Semana 6) ✅ COMPLETADA

**Objetivo:** Agenda de contactos de oficios

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Pantalla agenda de profesionales | Alta | ✅ |
| Pantalla crear/editar profesional | Alta | ✅ |
| Valoración personal (1-5 estrellas) | Media | ✅ |
| Vincular profesional a intervenciones | Alta | ✅ |
| Historial de trabajos por profesional | Media | ✅ |
| Acción: llamar (intent nativo) | Media | ✅ |

**Entregable:** Usuario tiene agenda de profesionales con historial de trabajos

---

## Fase 5 — Recordatorios + Localización España (Semana 7)

**Objetivo:** Mantenimientos programados con notificaciones + plantillas localizadas

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Pantalla lista de recordatorios | Alta | ⬜ |
| Crear recordatorio: título, intervalo, elemento vinculado | Alta | ⬜ |
| Notificaciones locales (expect/actual) | Alta | ⬜ |
| Lógica de recurrencia (cada X días/meses) | Alta | ⬜ |
| Marcar como completado → recalcular próxima fecha | Media | ⬜ |
| Indicadores visuales (verde/amarillo/rojo) | Media | ⬜ |
| **Plantillas recordatorios España** (ver detalle abajo) | Alta | ⬜ |

**Diferenciador incluido:** ✅ Localización profunda España

### Recordatorios predefinidos España

| Obligación | Frecuencia | Categoría |
|------------|------------|-----------|
| ITE (Inspección Técnica Edificios) | Variable según antigüedad/municipio | Otros |
| Revisión instalación gas | Cada 5 años | Climatización |
| Revisión caldera | Anual | Climatización |
| Limpieza filtros A/C | Cada 3 meses | Climatización |
| Boletín eléctrico | Al cambiar instalación | Electricidad |
| Certificado energético | Al vender/alquilar | Otros |

**Entregable:** Sistema de recordatorios funcional con plantillas para España

---

## Fase 6 — Home + Diferenciadores UI (Semana 8) 🔄 EN PROGRESO

**Objetivo:** Experiencia completa con diferenciadores visuales

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| **Pantalla Home con resumen y acciones rápidas** | Alta | ✅ |
| **Dashboard de gastos** (gasto mensual) | Alta | ✅ |
| **Rediseño UI terracota mediterráneo** | Alta | ✅ |
| Pantalla Home con timeline visual de fotos | Alta | ⬜ |
| **Onboarding con selector tipo vivienda** | Alta | ⬜ |
| Búsqueda global por texto | Alta | ⬜ |
| Filtros combinados (categoría, fecha, coste, estado) | Media | ⬜ |
| Animaciones y transiciones | Baja | ⬜ |
| Empty states con ilustraciones | Media | ⬜ |
| Revisión de UX y accesibilidad | Alta | ⬜ |

**Diferenciadores incluidos:**
- ✅ Dashboard de gastos inteligente
- ✅ Timeline visual antes/después (fotos)
- ✅ Plantillas por tipo de vivienda (onboarding)

### Onboarding: Tipos de vivienda

**Piso en ciudad:**
- Revisión caldera anual
- Limpieza filtros A/C cada 3 meses
- Revisión gas cada 5 años
- ITE cuando corresponda

**Casa con jardín:**
- Todo lo anterior +
- Mantenimiento jardín mensual
- Revisión sistema riego
- Poda árboles anual

**Chalet con piscina:**
- Todo lo anterior +
- Tratamiento piscina semanal
- Revisión depuradora mensual
- Abrir/cerrar piscina por temporada

### Dashboard de gastos

Visualización de los datos que ya se recopilan (`laborCost` + `materialCost`):
- Gasto total del mes actual
- Gasto por categoría (gráfico)
- Comparativa con mes/año anterior
- Top categorías donde más se gasta

**Entregable:** App lista para testing con todos los diferenciadores core

---

## Fase 7 — Testing y Lanzamiento Mobile (Semana 9-10)

**Objetivo:** Publicación en stores

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Tests unitarios (UseCases, Repositories) | Alta | ⬜ |
| Tests de UI (Compose test) | Media | ⬜ |
| Testing manual Android + iOS | Alta | ⬜ |
| Configurar firma (Android) y provisioning (iOS) | Alta | ⬜ |
| Assets para stores (capturas, descripción, icono) | Alta | ⬜ |
| Publicar en Google Play (beta cerrada) | Alta | ⬜ |
| Publicar en App Store (TestFlight) | Alta | ⬜ |

**Milestone:** 🚀 **MVP Mobile publicado en stores**

---

## Fase 8 — Compartir Historial (Post-lanzamiento)

**Objetivo:** Permitir compartir el historial de la vivienda

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Generar PDF con historial completo de vivienda | Alta | ⬜ |
| Incluir: intervenciones, fotos, profesionales, gastos | Alta | ⬜ |
| Diseño atractivo del PDF | Media | ⬜ |
| Opción de compartir vía apps nativas | Alta | ⬜ |
| (Opcional) Generar link compartible web | Media | ⬜ |

**Caso de uso:** Vender o alquilar vivienda mostrando todo el mantenimiento realizado

**Diferenciador incluido:** ✅ Compartir historial al vender/alquilar

---

## Fase 9 — Web (Futuro, cuando Kotlin/Wasm sea stable)

**Objetivo:** Versión web de la app

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Evaluar estado de Kotlin/Wasm | - | ⏸️ |
| Decidir arquitectura: local-only vs backend con sync | - | ⏸️ |
| Añadir target wasmJs | - | ⏸️ |
| Implementar expect/actual Web | - | ⏸️ |
| Adaptar UI para navegador (sidebar responsive) | - | ⏸️ |
| PWA para instalación desde navegador | - | ⏸️ |
| (Si backend) Implementar sync entre dispositivos | - | ⏸️ |

**Nota:** Si se implementa backend para Web, esto habilita sync entre dispositivos como feature Pro.

---

## Resumen de Diferenciadores

| Diferenciador | Fase | Estado |
|---------------|------|--------|
| Offline-first sin cuenta | 1 (arquitectura) | En MVP |
| Dashboard de gastos | 6 (Home) | En MVP |
| Plantillas por tipo vivienda | 6 (Onboarding) | En MVP |
| Localización profunda España | 5 (Recordatorios) | En MVP |
| Timeline visual fotos | 6 (Home) | En MVP |
| Compartir historial PDF | 8 | Post-MVP |

---

## Monetización (Fase posterior)

### Modelo Freemium

**Versión Gratuita:**
- 1 vivienda
- Todas las funcionalidades del MVP
- Sin límite de intervenciones
- Offline completo

**Versión Pro (pago único o suscripción):**
- Multi-vivienda
- Exportar a Excel
- Compartir historial (PDF premium)
- Backup en nube / Sync (cuando haya Web)
- OCR de facturas (futuro)

**Principio:** Sin anuncios nunca.

---

## Timeline Visual

```
    2025
    ────────────────────────────────────────────────

    Q1: MVP Mobile
    ├── S1-S2:  Fundamentos
    ├── S3-S4:  Intervenciones
    ├── S5:     Inventario
    ├── S6:     Profesionales
    ├── S7:     Recordatorios + España
    ├── S8:     Home + Dashboard + Onboarding
    └── S9-S10: Testing + Lanzamiento stores

    Q2: Post-lanzamiento
    ├── Compartir historial (PDF)
    ├── Feedback usuarios
    └── Iteración sobre MVP

    Q3-Q4: Crecimiento
    ├── Monetización (Pro)
    └── Web (si Wasm stable)

    ────────────────────────────────────────────────
```

---

## Próximos Pasos

1. **Iniciar Fase 5:** Recordatorios + Localización España
2. **Definir identidad:** Elegir nombre final y crear branding
3. **Diseñar UI:** Wireframes/mockups antes de codificar

---

*Documento generado combinando plan-desarrollo-hogarfix.md y diferenciadores-hogarfix.md*