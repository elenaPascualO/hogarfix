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
│  Fase 8                    Fase 9                  Fase 10                  │
│  Compartir historial       Monetización            Web (Wasm stable)        │
│  (PDF/link)                (Freemium progresivo)   ─ ─ ─ ─ ─ ─ ─ ─ ─>      │
│  ────────────────────>     ──────────────────>                              │
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

## Fase 5 — Recordatorios de Mantenimiento (Semana 7) ✅ COMPLETADA

**Objetivo:** Sistema de recordatorios de mantenimiento con recurrencia

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Pantalla lista de recordatorios (3 secciones: vencidos/próximos/programados) | Alta | ✅ |
| Crear/editar recordatorio: título, intervalo configurable, categoría | Alta | ✅ |
| Lógica de recurrencia (cada X días/semanas/meses/años) | Alta | ✅ |
| Marcar como completado → recalcular próxima fecha | Media | ✅ |
| Indicadores visuales (verde/amarillo/rojo) | Media | ✅ |
| Dialog tras crear intervención: "¿Programar recordatorio?" | Alta | ✅ |
| Dashboard: alerta de vencidos + próximos mantenimientos | Alta | ✅ |
| Selector de profesional en formulario de intervención | Media | ✅ |
| Notificaciones locales (expect/actual) | Alta | ⬜ |
| **Plantillas recordatorios España** (ITE, gas, caldera, etc.) | Alta | ⬜ |

**Nota:** Notificaciones push y plantillas España se implementarán en fase posterior.

### Recordatorios predefinidos España

| Obligación | Frecuencia | Categoría |
|------------|------------|-----------|
| ITE (Inspección Técnica Edificios) | Variable según antigüedad/municipio | Otros |
| Revisión instalación gas | Cada 5 años | Climatización |
| Revisión caldera | Anual | Climatización |
| Limpieza filtros A/C | Cada 3 meses | Climatización |
| Boletín eléctrico | Al cambiar instalación | Electricidad |
| Certificado energético | Al vender/alquilar | Otros |

**Entregable:** Sistema de recordatorios funcional con CRUD completo, integración en dashboard y flujo post-intervención

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

> Guía técnica detallada: [`doc/mobile-deployment-plan.md`](mobile-deployment-plan.md)

### 7.1 Testing

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Tests unitarios (UseCases, Repositories, Mappers, Models) | Alta | ✅ (98 tests) |
| Tests de UI (Compose test) | Media | ⬜ |
| Testing manual Android + iOS | Alta | ⬜ |

### 7.2 Preparación técnica

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Generar keystore de release (Android) | Alta | ⬜ |
| Configurar signing + ProGuard en `build.gradle.kts` | Alta | ⬜ |
| Crear `proguard-rules.pro` | Alta | ⬜ |
| Configurar TEAM_ID + bundle ID (iOS) | Alta | ⬜ |
| Reducir deployment target a iOS 16.0 | Alta | ⬜ |
| Crear `PrivacyInfo.xcprivacy` | Alta | ⬜ |
| Build release probado en dispositivo físico (ambas plataformas) | Alta | ⬜ |

### 7.3 Assets y tiendas

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Icono final 1024×1024 (sin transparencia, paleta terracota) | Alta | ⬜ |
| Screenshots Android (teléfono + tablet) + iOS (iPhone + iPad) | Alta | ⬜ |
| Feature graphic 1024×500 (Android) | Media | ⬜ |
| Descripción larga y corta para stores | Alta | ⬜ |
| Publicar política de privacidad (URL pública) | Alta | ⬜ |
| Crear app en Google Play Console + ficha completa | Alta | ⬜ |
| Crear app en App Store Connect + ficha completa | Alta | ⬜ |

### 7.4 Lanzamiento

| Tarea | Prioridad | Estado |
|-------|-----------|--------|
| Subir AAB a testing interno (Google Play) | Alta | ⬜ |
| Subir build a TestFlight (App Store) | Alta | ⬜ |
| Beta testing con 5-10 testers (1-2 semanas) | Alta | ⬜ |
| Lanzamiento producción Android (rollout gradual) | Alta | ⬜ |
| Enviar a revisión App Store + lanzamiento | Alta | ⬜ |

### Cuentas necesarias

| Plataforma | Coste | Duración |
|------------|-------|----------|
| Google Play Developer | ~25 USD (pago único) | Permanente |
| Apple Developer Program | 99 USD/año | Anual |

### Timeline estimado: 3-4 semanas

Preparación (1-2 días) → Config técnica (2 días) → Assets (2-3 días) → Config tiendas (1 día) → Testing (1-2 semanas) → Lanzamiento (2-3 días)

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

## Fase 9 — Monetización (Freemium progresivo)

**Objetivo:** Monetización sostenible sin comprometer la experiencia

> Principio: **Sin anuncios nunca.**

### Estrategia en 3 sub-fases

```
Fase A (Lanzamiento)    →  Fase B (~500 usuarios)     →  Fase C (con backend)
Todo gratis                Freemium + pago único          Añadir tier suscripción
(ganar tracción)           4.99€                          1.99€/mes o 14.99€/año
```

### Sub-fase A — Lanzamiento (0 → ~500 usuarios activos)

| Tier | Precio | Contenido |
|------|--------|-----------|
| **Gratis** | 0€ | Todas las funcionalidades sin límites |

Objetivo: conseguir usuarios, reviews, feedback y validar product-market fit.

### Sub-fase B — Freemium (a partir de ~500 usuarios activos)

| Tier | Precio | Contenido |
|------|--------|-----------|
| **Gratis** | 0€ | 1 vivienda, máx 20 intervenciones, 5 items inventario, 3 profesionales |
| **Pro** | **4.99€ pago único** | Todo ilimitado, multi-vivienda, export PDF/Excel, temas visuales |

Pago único porque la app es offline sin coste de servidor — la suscripción no se justifica aún.

Usuarios de la Fase A mantienen acceso ilimitado como "early adopters".

### Sub-fase C — Cloud y Sync (con backend)

| Tier | Precio | Contenido |
|------|--------|-----------|
| **Gratis** | 0€ | Igual que sub-fase B, sin sync |
| **Pro** | 4.99€ pago único | Todo offline ilimitado |
| **Pro+** | **1.99€/mes** o **14.99€/año** | Sync entre dispositivos, backup cloud, acceso web |

Ahora sí suscripción: hay coste real de servidor.

### Features con alto poder de conversión

| Feature | Tier | Razón de conversión |
|---------|------|---------------------|
| Multi-vivienda | Pro | Quien tiene 2+ propiedades paga sin dudar |
| Export PDF | Pro | Seguros, venta de casa, reclamaciones |
| Export Excel | Pro | Control total de datos |
| Estadísticas avanzadas | Pro | Gráficos de gasto anual por categoría |
| OCR de facturas | Pro | Alto valor percibido |
| Temas visuales | Pro | Personalización |
| Sync multi-dispositivo | Pro+ | Familias, parejas |
| Backup en la nube | Pro+ | Tranquilidad |
| Acceso web | Pro+ | Consultar desde ordenador |

### Implementación técnica

Arquitectura del paywall: `PurchaseManager` (expect/actual) → `SubscriptionRepository` (estado en Room) → `Feature Flags` (isProUser, isProPlusUser).

- Android: Google Play Billing
- iOS: StoreKit 2

Integrar en Fase 7 o como "Fase 7.5" antes del lanzamiento público.

### Métricas clave

| Métrica | Objetivo Fase A | Objetivo Fase B |
|---------|----------------|----------------|
| Usuarios activos mensuales | 500+ | 2.000+ |
| Retención a 30 días | > 30% | > 35% |
| Tasa de conversión Free → Pro | — | > 3% |
| Rating en tiendas | > 4.0 | > 4.2 |

---

## Fase 10 — Web (Futuro, cuando Kotlin/Wasm sea stable)

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

**Nota:** Si se implementa backend para Web, esto habilita sync entre dispositivos como feature Pro+ (ver Fase 9).

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
| Monetización freemium | 9 | Post-MVP |

---

## Timeline Visual

```
    2025-2026
    ────────────────────────────────────────────────

    MVP Mobile
    ├── S1-S2:  Fundamentos ✅
    ├── S3-S4:  Intervenciones ✅
    ├── S5:     Inventario ✅
    ├── S6:     Profesionales ✅
    ├── S7:     Recordatorios + España ✅
    ├── S8:     Home + Dashboard + Onboarding 🔄
    └── S9-S10: Testing + Lanzamiento stores

    Post-lanzamiento
    ├── Fase 8:  Compartir historial (PDF)
    ├── Fase 9:  Monetización freemium progresivo
    │   ├── A: Todo gratis (tracción)
    │   ├── B: Pro 4.99€ (~500 usuarios)
    │   └── C: Pro+ suscripción (con backend)
    └── Fase 10: Web (si Wasm stable)

    ────────────────────────────────────────────────
```

---

## Próximos Pasos

1. **Completar Fase 6:** Timeline visual fotos, onboarding, búsqueda global
2. **Notificaciones locales:** expect/actual para Android e iOS
3. **Plantillas España:** ITE, revisión gas, caldera, etc.
4. **Onboarding:** Selector tipo vivienda con plantillas predefinidas
5. **Fase 7:** Preparación técnica de despliegue + testing + lanzamiento

---

*Documento consolidado — integra plan de desarrollo, diferenciadores, monetización y despliegue.*