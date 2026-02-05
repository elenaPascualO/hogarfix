# HogarFix — Modelo de Datos

---

## Diagrama de Entidades

```
┌─────────────────────┐       ┌─────────────────────┐
│    Intervention     │       │      HomeItem       │
├─────────────────────┤       ├─────────────────────┤
│ id: Long (PK)       │       │ id: Long (PK)       │
│ title: String       │       │ name: String        │
│ description: String?│       │ brand: String?      │
│ date: LocalDate     │       │ model: String?      │
│ category: Category  │       │ category: Category  │
│ laborCost: Double?  │       │ purchaseDate: LocalDate? │
│ materialCost: Double?│      │ warrantyEndDate: LocalDate? │
│ status: Status      │       │ location: String?   │
│ doneBy: DoneBy      │       │ notes: String?      │
│ professionalId: Long?│──┐   │ photoUris: List<String> │
│ homeItemId: Long?───│──│───>│ createdAt: Instant  │
│ photoUris: List<String>│ │  │ updatedAt: Instant  │
│ notes: String?      │  │    └──────────┬──────────┘
│ createdAt: Instant  │  │               │
│ updatedAt: Instant  │  │               │ 1:N
└─────────────────────┘  │               │
                         │    ┌──────────▼──────────┐
                         │    │      Reminder       │
┌─────────────────────┐  │    ├─────────────────────┤
│    Professional     │  │    │ id: Long (PK)       │
├─────────────────────┤  │    │ title: String       │
│ id: Long (PK)       │<─┘    │ description: String?│
│ name: String        │       │ intervalDays: Int   │
│ phone: String?      │       │ nextDueDate: LocalDate │
│ email: String?      │       │ homeItemId: Long?   │
│ specialty: String   │       │ category: Category  │
│ personalRating: Int?│       │ isActive: Boolean   │
│ notes: String?      │       │ lastCompletedDate: LocalDate? │
│ createdAt: Instant  │       │ createdAt: Instant  │
└─────────────────────┘       └─────────────────────┘
```

---

## Entidades

### Intervention

Registro de un trabajo realizado en el hogar.

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Primary key |
| title | String | Sí | Título descriptivo |
| description | String? | No | Descripción detallada |
| date | LocalDate | Sí | Fecha del trabajo |
| category | Category | Sí | Categoría del trabajo |
| laborCost | Double? | No | Coste de mano de obra |
| materialCost | Double? | No | Coste de materiales |
| status | Status | Sí | Estado actual |
| doneBy | DoneBy | Sí | Quién lo hizo |
| professionalId | Long? | No | FK a Professional |
| homeItemId | Long? | No | FK a HomeItem |
| photoUris | List\<String\> | No | URIs de fotos adjuntas |
| notes | String? | No | Notas adicionales |
| createdAt | Instant | Auto | Fecha de creación |
| updatedAt | Instant | Auto | Última modificación |

---

### HomeItem

Electrodoméstico o elemento del hogar.

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Primary key |
| name | String | Sí | Nombre del elemento |
| brand | String? | No | Marca |
| model | String? | No | Modelo |
| category | Category | Sí | Categoría |
| purchaseDate | LocalDate? | No | Fecha de compra |
| warrantyEndDate | LocalDate? | No | Fin de garantía |
| location | String? | No | Ubicación en casa |
| notes | String? | No | Notas adicionales |
| photoUris | List\<String\> | No | URIs de fotos |
| createdAt | Instant | Auto | Fecha de creación |
| updatedAt | Instant | Auto | Última modificación |

---

### Professional

Contacto de profesional o empresa.

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Primary key |
| name | String | Sí | Nombre o empresa |
| phone | String? | No | Teléfono |
| email | String? | No | Email |
| specialty | String | Sí | Especialidad principal |
| personalRating | Int? | No | Valoración 1-5 |
| notes | String? | No | Notas adicionales |
| createdAt | Instant | Auto | Fecha de creación |

---

### Reminder

Recordatorio de mantenimiento periódico.

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| id | Long | Auto | Primary key |
| title | String | Sí | Título del recordatorio |
| description | String? | No | Descripción |
| intervalDays | Int | Sí | Intervalo en días |
| nextDueDate | LocalDate | Sí | Próxima fecha |
| homeItemId | Long? | No | FK a HomeItem |
| category | Category | Sí | Categoría |
| isActive | Boolean | Sí | Si está activo |
| lastCompletedDate | LocalDate? | No | Última vez completado |
| createdAt | Instant | Auto | Fecha de creación |

---

## Enums

### Category

Categorías de trabajos y elementos.

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
```

### Status

Estado de una intervención.

```kotlin
enum class Status {
    PENDING,         // Pendiente
    IN_PROGRESS,     // En curso
    COMPLETED        // Terminado
}
```

### DoneBy

Quién realizó el trabajo.

```kotlin
enum class DoneBy {
    MYSELF,          // Yo mismo
    PROFESSIONAL     // Profesional
}
```

---

## Relaciones

| Relación | Tipo | Descripción |
|----------|------|-------------|
| Intervention → Professional | N:1 | Una intervención puede tener un profesional asignado |
| Intervention → HomeItem | N:1 | Una intervención puede estar vinculada a un elemento |
| Reminder → HomeItem | N:1 | Un recordatorio puede estar vinculado a un elemento |
| HomeItem → Intervention | 1:N | Un elemento puede tener múltiples intervenciones |
| Professional → Intervention | 1:N | Un profesional puede tener múltiples trabajos |

---

## Queries Comunes

### Intervenciones recientes
```kotlin
@Query("SELECT * FROM interventions ORDER BY date DESC LIMIT :limit")
fun getRecentInterventions(limit: Int): Flow<List<InterventionEntity>>
```

### Intervenciones por elemento
```kotlin
@Query("SELECT * FROM interventions WHERE homeItemId = :itemId ORDER BY date DESC")
fun getInterventionsByHomeItem(itemId: Long): Flow<List<InterventionEntity>>
```

### Gastos por categoría
```kotlin
@Query("""
    SELECT category, SUM(laborCost + materialCost) as total
    FROM interventions
    WHERE date BETWEEN :startDate AND :endDate
    GROUP BY category
""")
fun getExpensesByCategory(startDate: LocalDate, endDate: LocalDate): Flow<List<CategoryExpense>>
```

### Recordatorios vencidos
```kotlin
@Query("SELECT * FROM reminders WHERE isActive = 1 AND nextDueDate <= :today")
fun getOverdueReminders(today: LocalDate): Flow<List<ReminderEntity>>
```

### Garantías próximas a vencer
```kotlin
@Query("""
    SELECT * FROM home_items
    WHERE warrantyEndDate IS NOT NULL
    AND warrantyEndDate BETWEEN :today AND :futureDate
""")
fun getItemsWithExpiringWarranty(today: LocalDate, futureDate: LocalDate): Flow<List<HomeItemEntity>>
```

---

## Notas de Implementación

### Almacenamiento de fotos
- Las fotos se guardan en el almacenamiento interno de la app
- En la DB solo se guarda la URI/path como String
- Se usa TypeConverter para `List<String>` ↔ JSON

### Fechas
- Usar `kotlinx-datetime` para `LocalDate` e `Instant`
- TypeConverters para Room: `LocalDate` ↔ `Long` (epoch days)

### Migraciones
- Mantener versión de schema en Room
- Crear migraciones para cada cambio de estructura