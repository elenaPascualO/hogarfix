# HogarFix — Plan de Monetización

> Estrategia de monetización progresiva para una app offline-first sin backend

---

## Contexto

HogarFix es una app **offline-first, sin cuenta obligatoria y sin servidor**. Esto condiciona la estrategia:

- No hay costes de infraestructura recurrentes (hasta que se añada sync/cloud)
- El valor percibido es de "herramienta" (como una app de notas), no de "servicio"
- El uso es esporádico (se registra una intervención cada X semanas)
- Competimos con papel, Excel y la memoria del usuario

---

## Estrategia: Freemium progresivo en 3 fases

```
┌──────────────────────────────────────────────────────────────────────┐
│                                                                      │
│  Fase A                Fase B                  Fase C                │
│  Lanzamiento           Monetización            Cloud/Sync            │
│  ──────────>           ──────────>             ──────────>           │
│  Todo gratis           Freemium +              Añadir tier           │
│  (ganar tracción)      pago único 4.99€        suscripción           │
│                        (+500 usuarios)         (con backend)         │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Fase A — Lanzamiento (0 → ~500 usuarios activos)

| Tier | Precio | Contenido |
|------|--------|-----------|
| **Gratis** | 0€ | Todas las funcionalidades actuales sin límites |

### Objetivo

- Conseguir usuarios, reviews y feedback
- Validar product-market fit
- Sin límites artificiales: el usuario prueba todo el valor de la app

### Duración estimada

Desde el lanzamiento hasta alcanzar ~500 usuarios activos mensuales.

---

## Fase B — Monetización (a partir de ~500 usuarios activos)

| Tier | Precio | Contenido |
|------|--------|-----------|
| **Gratis** | 0€ | 1 vivienda, máx 20 intervenciones, 5 items inventario, 3 profesionales |
| **Pro** | **4.99€ pago único** | Todo ilimitado, multi-vivienda, export PDF/Excel, temas visuales |

### Por qué pago único y no suscripción

- La app es offline sin coste de servidor → la suscripción no se justifica
- Barrera de entrada baja (precio de un café)
- El usuario lo percibe como "justo" para una herramienta
- Se puede subir a 9.99€ cuando haya más features Pro

### Por qué no anuncios

- Destruyen la experiencia en una app de utilidad personal
- Generan ingresos mínimos con base de usuarios pequeña
- Ahuyentan a los usuarios que más valorarían la versión Pro

### Límites del tier gratuito

Los límites se eligen para que:
1. El usuario pueda probar todas las funcionalidades
2. Sienta la limitación solo cuando ya es usuario activo y ve valor
3. No se sienta engañado (el free sigue siendo útil para uso básico)

### Usuarios existentes de la Fase A

Los usuarios que instalaron la app durante la Fase A mantienen acceso ilimitado como "early adopters". Esto evita el efecto "bait and switch" y genera buena voluntad.

---

## Fase C — Cloud y Sync (post-lanzamiento, con backend)

| Tier | Precio | Contenido |
|------|--------|-----------|
| **Gratis** | 0€ | Igual que Fase B, sin sync |
| **Pro** | 4.99€ pago único | Todo offline ilimitado |
| **Pro+** | **1.99€/mes** o **14.99€/año** | Sync entre dispositivos, backup cloud, acceso web |

### Por qué ahora sí suscripción

- Hay coste real de servidor (almacenamiento, sync, backups)
- El usuario entiende que paga por un servicio continuo
- El tier Pro (pago único) sigue existiendo para quien no quiera sync

---

## Features con alto poder de conversión

Ordenadas por impacto estimado en la decisión de compra:

| Feature | Tier | Razón de conversión |
|---------|------|---------------------|
| **Multi-vivienda** | Pro | Quien tiene 2+ propiedades paga sin dudar |
| **Export PDF** | Pro | Necesario para seguros, venta de casa, reclamaciones |
| **Export Excel** | Pro | Usuarios que quieren control total de sus datos |
| **Estadísticas avanzadas** | Pro | Gráficos de gasto anual por categoría |
| **OCR de facturas** | Pro | Escanear y registrar automáticamente (alto valor percibido) |
| **Temas visuales** | Pro | Personalización como incentivo adicional |
| **Sync multi-dispositivo** | Pro+ | Familias, parejas que comparten casa |
| **Backup en la nube** | Pro+ | Tranquilidad de no perder datos |
| **Acceso web** | Pro+ | Consultar desde el ordenador |

---

## Implementación técnica

### Arquitectura del paywall

```
┌─────────────────────────────────┐
│         PurchaseManager          │
│  (expect/actual por plataforma)  │
├─────────────────────────────────┤
│  Android: Google Play Billing    │
│  iOS: StoreKit 2                 │
└──────────┬──────────────────────┘
           │
           ▼
┌─────────────────────────────────┐
│       SubscriptionRepository     │
│  Estado de compra en Room DB     │
│  + verificación de recibos       │
└──────────┬──────────────────────┘
           │
           ▼
┌─────────────────────────────────┐
│        Feature Flags             │
│  isProUser: Boolean              │
│  isProPlusUser: Boolean          │
│  Controla visibilidad de UI      │
│  y límites de datos              │
└─────────────────────────────────┘
```

### Almacenamiento local

```kotlin
// Tabla en Room para estado de compra
@Entity(tableName = "purchase_state")
data class PurchaseStateEntity(
    @PrimaryKey val id: Int = 1,
    val isPro: Boolean = false,
    val isProPlus: Boolean = false,
    val purchaseToken: String? = null,
    val purchaseDate: Long? = null,
    val isEarlyAdopter: Boolean = false
)
```

### Fase recomendada de implementación

Integrar el paywall en la **Fase 7 (Testing + Lanzamiento)** o como **Fase 7.5** antes del lanzamiento público, para que el mecanismo esté listo aunque los límites no se activen hasta la Fase B.

---

## Métricas clave a seguir

| Métrica | Objetivo Fase A | Objetivo Fase B |
|---------|----------------|----------------|
| Usuarios activos mensuales | 500+ | 2.000+ |
| Retención a 30 días | > 30% | > 35% |
| Tasa de conversión Free → Pro | — | > 3% |
| Rating en tiendas | > 4.0 | > 4.2 |
| Intervenciones por usuario/mes | > 2 | > 3 |

---

## Decisiones descartadas

| Opción | Razón del descarte |
|--------|-------------------|
| **Suscripción desde el día 1** | Sin backend no se justifica; alta fricción para app de utilidad |
| **Anuncios** | Ingresos mínimos, destruyen la experiencia, espantan usuarios |
| **Todo gratis para siempre** | No es sostenible a largo plazo |
| **Pago único desde el lanzamiento** | Sin base de usuarios no hay validación de product-market fit |
| **Límites agresivos en free** | Si el free es inútil, nadie prueba la app |
