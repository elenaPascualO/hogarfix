# HogarFix — Plan de Despliegue Móvil

> Guía completa para publicar HogarFix en Google Play y App Store.
> Documento generado: Febrero 2026

---

## Índice

1. [Requisitos Previos](#1-requisitos-previos)
2. [Android — Despliegue en Google Play](#2-android--despliegue-en-google-play)
3. [iOS — Despliegue en App Store](#3-ios--despliegue-en-app-store)
4. [Requisitos Comunes (Ambas Plataformas)](#4-requisitos-comunes-ambas-plataformas)
5. [Cambios Técnicos Necesarios](#5-cambios-técnicos-necesarios)
6. [Timeline y Fases](#6-timeline-y-fases)
7. [Checklist Pre-Lanzamiento](#7-checklist-pre-lanzamiento)
8. [Apéndice: Comandos Útiles](#8-apéndice-comandos-útiles)

---

## 1. Requisitos Previos

### 1.1 Cuentas de Desarrollador

| Plataforma | Coste | Duración | Tiempo de Aprobación |
|---|---|---|---|
| **Google Play Developer** | ~25 USD (pago único) | Permanente | 24-48 horas |
| **Apple Developer Program** | 99 USD/año | Anual (renovar cada año) | 24-48 horas (hasta 2 semanas si verificación adicional) |

### 1.2 Herramientas Necesarias

| Herramienta | Plataforma | Versión Mínima | Propósito |
|---|---|---|---|
| **Android Studio** | macOS/Windows/Linux | Ladybug (2024.2)+ | Build Android, generación AAB |
| **Xcode** | Solo macOS | 15.0+ (recomendado 16.x) | Build iOS, Archive, Upload |
| **JDK** | Todas | 17+ | Compilación Kotlin/Gradle |
| **Gradle** | Todas | 8.5+ (incluido en proyecto) | Build system |

### 1.3 Recursos a Preparar

| Recurso | Formato | Cantidad | Notas |
|---|---|---|---|
| **Icono de app** | PNG 1024×1024 | 1 (base) | Sin transparencia para iOS |
| **Screenshots Android** | PNG/JPEG | 2-8 por tipo de dispositivo | Teléfono, tablet 7", tablet 10" |
| **Screenshots iOS** | PNG | 3+ por tamaño de pantalla | 6.7", 6.5", 5.5", iPad Pro |
| **Feature graphic (Android)** | PNG/JPEG 1024×500 | 1 | Banner de Google Play |
| **Vídeo promocional** | MP4/MOV | Opcional | Max 30 segundos |
| **Política de privacidad** | URL pública | 1 | Obligatoria para ambas tiendas |

---

## 2. Android — Despliegue en Google Play

### 2.1 Generación del Keystore

El keystore es la identidad criptográfica de la app. **CRÍTICO: si se pierde, no se podrá actualizar la app.**

```bash
# Crear directorio para el keystore (NO incluir en git)
mkdir -p ~/.android-keystores

# Generar keystore
keytool -genkeypair \
  -v \
  -keystore ~/.android-keystores/hogarfix-release.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias hogarfix \
  -storepass TU_CONTRASEÑA_SEGURA \
  -keypass TU_CONTRASEÑA_SEGURA \
  -dname "CN=HogarFix, OU=Mobile, O=HogarFix, L=Madrid, ST=Madrid, C=ES"
```

**Guardar de forma segura:**
- Keystore: `~/.android-keystores/hogarfix-release.jks`
- Alias: `hogarfix`
- Passwords del store y key
- Hacer backup en lugar seguro (contraseñas en gestor de contraseñas, keystore en copia externa)
- **NUNCA subir al repositorio git**

### 2.2 Configuración de Firma en build.gradle.kts

**Archivo:** `composeApp/build.gradle.kts`

Añadir `signingConfigs` y modificar `buildTypes` dentro del bloque `android {}`:

```kotlin
android {
    // ... (lo existente se mantiene)

    signingConfigs {
        create("release") {
            storeFile = file(
                System.getProperty("user.home") +
                "/.android-keystores/hogarfix-release.jks"
            )
            storePassword = System.getenv("HOGARFIX_KEYSTORE_PASSWORD") ?: ""
            keyAlias = "hogarfix"
            keyPassword = System.getenv("HOGARFIX_KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true               // CAMBIAR de false a true
            isShrinkResources = true             // NUEVO
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### 2.3 Reglas ProGuard / R8

**Archivo a crear:** `composeApp/proguard-rules.pro`

```proguard
# ============================================
# HogarFix — Reglas ProGuard para Release
# ============================================

# --- Kotlin ---
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }

# --- Kotlin Coroutines ---
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# --- Kotlinx Serialization ---
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
    *** serializer();
    *** INSTANCE;
}

# --- Room ---
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# --- Compose ---
-dontwarn androidx.compose.**

# --- Koin ---
-keep class org.koin.** { *; }

# --- Coil ---
-dontwarn coil3.**

# --- Modelos de dominio (serialización / Room entities) ---
-keep class com.hogarfix.domain.model.** { *; }
-keep class com.hogarfix.data.local.entity.** { *; }

# --- Enums ---
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# --- Números de línea para stack traces ---
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
```

### 2.4 Añadir Permiso INTERNET

**Archivo:** `composeApp/src/androidMain/AndroidManifest.xml`

Añadir antes de `<application>`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Necesario para Coil (carga de imágenes por red) y futuras funcionalidades online.

### 2.5 Construir el AAB

```bash
# Exportar variables de entorno
export HOGARFIX_KEYSTORE_PASSWORD="tu_contraseña"
export HOGARFIX_KEY_PASSWORD="tu_contraseña"

# Build
./gradlew clean :composeApp:bundleRelease

# Resultado en:
# composeApp/build/outputs/bundle/release/composeApp-release.aab
```

### 2.6 Configuración en Google Play Console

#### Crear la Aplicación

1. Ir a [Google Play Console](https://play.google.com/console)
2. **Crear aplicación** → Nombre: "HogarFix"
3. Idioma predeterminado: Español (España)
4. Tipo: Aplicación (no juego)
5. Gratis (con compras integradas en Fase B del plan de monetización)

#### Ficha de Play Store

| Campo | Valor |
|---|---|
| **Nombre** | HogarFix — Mantenimiento del Hogar |
| **Descripción breve** (80 chars) | Registra reparaciones, gastos y mantenimiento de tu hogar de forma sencilla |
| **Categoría** | Casa y hogar |
| **Etiquetas** | mantenimiento, hogar, reparaciones, gastos |

#### Sección de Seguridad de Datos

HogarFix es 100% offline:

| Pregunta | Respuesta |
|---|---|
| ¿Tu app recopila o comparte datos de usuario? | **No** |
| ¿Usa cifrado en tránsito? | No aplica (sin conexión) |
| ¿Permite solicitar eliminación de datos? | Sí (desinstalar borra todo) |

> **Nota:** Cuando se implemente la monetización (Fase B), actualizar para indicar datos de compra procesados por Google Play Billing.

#### Proceso de Lanzamiento

| Fase | Audiencia | Duración recomendada |
|---|---|---|
| **Testing interno** | Hasta 100 testers por email | 1-2 semanas |
| **Testing cerrado** | Hasta 2.000 testers | 1 semana |
| **Testing abierto** | Público (opcional) | 1 semana |
| **Producción** | Rollout gradual 10% → 25% → 50% → 100% | Progresivo |

---

## 3. iOS — Despliegue en App Store

### 3.1 Configurar TEAM_ID

**Archivo:** `iosApp/Configuration/Config.xcconfig`

```
TEAM_ID=XXXXXXXXXX

PRODUCT_NAME=HogarFix
PRODUCT_BUNDLE_IDENTIFIER=com.hogarfix.HogarFix

CURRENT_PROJECT_VERSION=1
MARKETING_VERSION=1.0.0
```

**Cómo obtener el TEAM_ID:**
1. Ir a [Apple Developer → Account](https://developer.apple.com/account)
2. Sección "Membership" → Team ID (10 caracteres alfanuméricos)

**Nota:** Eliminar `$(TEAM_ID)` del `PRODUCT_BUNDLE_IDENTIFIER` — el bundle ID debe ser fijo, sin variables dinámicas.

### 3.2 Reducir Deployment Target

**Problema actual:** `IPHONEOS_DEPLOYMENT_TARGET = 18.2` excluye a la mayoría de usuarios.

**Archivo:** `iosApp/iosApp.xcodeproj/project.pbxproj`

Cambiar en ambas configuraciones (Debug y Release):

```
IPHONEOS_DEPLOYMENT_TARGET = 16.0;
```

iOS 16.0 cubre ~95% de dispositivos activos y permite usar todas las APIs modernas que usa HogarFix (PhotosUI, SwiftUI, etc.).

### 3.3 Crear PrivacyInfo.xcprivacy

**Archivo a crear:** `iosApp/iosApp/PrivacyInfo.xcprivacy`

Obligatorio desde marzo 2024 para nuevas apps y actualizaciones.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN"
  "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>NSPrivacyAccessedAPITypes</key>
    <array>
        <!-- File timestamp APIs (Room/SQLite) -->
        <dict>
            <key>NSPrivacyAccessedAPIType</key>
            <string>NSPrivacyAccessedAPICategoryFileTimestamp</string>
            <key>NSPrivacyAccessedAPITypeReasons</key>
            <array>
                <string>C617.1</string>
            </array>
        </dict>
        <!-- System boot time (kotlinx-datetime) -->
        <dict>
            <key>NSPrivacyAccessedAPIType</key>
            <string>NSPrivacyAccessedAPICategorySystemBootTime</string>
            <key>NSPrivacyAccessedAPITypeReasons</key>
            <array>
                <string>35F9.1</string>
            </array>
        </dict>
        <!-- User defaults (Compose preferences) -->
        <dict>
            <key>NSPrivacyAccessedAPIType</key>
            <string>NSPrivacyAccessedAPICategoryUserDefaults</string>
            <key>NSPrivacyAccessedAPITypeReasons</key>
            <array>
                <string>CA92.1</string>
            </array>
        </dict>
    </array>

    <key>NSPrivacyCollectedDataTypes</key>
    <array/>

    <key>NSPrivacyTracking</key>
    <false/>

    <key>NSPrivacyTrackingDomains</key>
    <array/>
</dict>
</plist>
```

### 3.4 Actualizar Info.plist

**Archivo:** `iosApp/iosApp/Info.plist`

Añadir la descripción de acceso a fotos:

```xml
<key>NSPhotoLibraryUsageDescription</key>
<string>HogarFix necesita acceso a tus fotos para adjuntar imágenes
a las intervenciones de mantenimiento.</string>
```

### 3.5 Configurar Firma de Distribución

1. Abrir `iosApp/iosApp.xcodeproj` en Xcode
2. Seleccionar target "iosApp" → tab "Signing & Capabilities"
3. Team: seleccionar tu cuenta de Apple Developer
4. Marcar "Automatically manage signing"
5. Xcode creará automáticamente los perfiles de desarrollo y distribución

Para enviar al App Store, Xcode usa el perfil "App Store Distribution" automáticamente al hacer Archive → Distribute.

### 3.6 Construir y Subir

```bash
# 1. Compilar framework Kotlin
./gradlew :composeApp:assembleReleaseXCFramework

# 2. Abrir en Xcode
open iosApp/iosApp.xcodeproj

# 3. En Xcode:
#    - Product → Destination → Any iOS Device (arm64)
#    - Product → Archive
#    - Window → Organizer → Distribute App → App Store Connect
```

### 3.7 Configuración en App Store Connect

#### Crear la App

1. Ir a [App Store Connect](https://appstoreconnect.apple.com)
2. Apps → (+) → Nueva app
3. Plataformas: iOS
4. Nombre: HogarFix
5. Idioma principal: Español
6. Bundle ID: com.hogarfix.HogarFix
7. SKU: hogarfix-ios

#### Información de la App

| Campo | Valor |
|---|---|
| **Nombre** | HogarFix — Mantenimiento del Hogar |
| **Subtítulo** (30 chars) | Control total de tu hogar |
| **Categoría primaria** | Utilidades |
| **Categoría secundaria** | Productividad |
| **Clasificación por edades** | 4+ |

#### App Privacy (Nutrition Labels)

| Pregunta | Respuesta |
|---|---|
| ¿Recopilas datos de los usuarios? | **No, no recopilamos datos** |

### 3.8 Proceso de Revisión

| Paso | Detalle |
|---|---|
| **1. Subir build** | Archive → Upload to App Store Connect (procesamiento: 15-60 min) |
| **2. TestFlight** | Testers internos (100) + externos (10.000). Probar 1-2 semanas. |
| **3. Enviar a revisión** | Completar ficha + screenshots + enviar |
| **4. Revisión Apple** | 24-48h típico, hasta 7 días en periodos de alta demanda |
| **5. Lanzamiento** | Inmediato o programar fecha específica |

---

## 4. Requisitos Comunes (Ambas Plataformas)

### 4.1 Política de Privacidad

Obligatoria para ambas tiendas. Debe estar en una URL pública y accesible.

**Opciones de hosting gratuito:**
- GitHub Pages: `https://tu-usuario.github.io/hogarfix-privacy`
- Notion pública
- Sitio web propio

**Contenido mínimo:**

```markdown
# Política de Privacidad de HogarFix

Última actualización: [FECHA]

## Resumen
HogarFix funciona completamente offline.
No recopilamos, almacenamos ni compartimos ningún dato personal.

## Datos que almacena la app (solo en tu dispositivo)
- Registros de intervenciones
- Inventario de electrodomésticos
- Contactos de profesionales
- Fotos adjuntas

## Datos que NO recopilamos
- Información personal identificable
- Ubicación
- Contactos del teléfono
- Datos de uso o analíticas

## Permisos
- **Fotos** (iOS): para adjuntar imágenes a intervenciones
- **Internet** (Android): para funcionalidades futuras

## Contacto
[tu-email@ejemplo.com]
```

### 4.2 Descripción de la App

```
HogarFix: Tu asistente de mantenimiento del hogar

¿Cansado de olvidar cuándo fue la última revisión del aire acondicionado?
¿No encuentras la factura de aquella reparación?
HogarFix te ayuda a mantener tu hogar organizado.

REGISTRA TODO
• Intervenciones de fontanería, electricidad, pintura y 12 categorías más
• Fecha, coste, profesional y notas detalladas
• Fotos del antes y después

CONTROLA TUS GASTOS
• Dashboard con resumen mensual
• Historial completo de mantenimiento
• Filtra por categoría, estado o fecha

100% OFFLINE Y PRIVADO
• Funciona sin conexión a internet
• Tus datos nunca salen de tu dispositivo
• Sin cuentas ni registros obligatorios

DISEÑADO PARA ESPAÑA
• Recordatorios de ITE, revisión de gas, caldera
• Categorías adaptadas al mercado español
• Totalmente en español

INVENTARIO Y PROFESIONALES
• Registra electrodomésticos con marca, modelo y garantía
• Directorio de profesionales con valoración por estrellas
• Llamar directamente desde la app

RECORDATORIOS INTELIGENTES
• Programa mantenimientos periódicos (caldera, filtros, ITE...)
• Alertas de vencidos con indicadores visuales
• Completa y recalcula automáticamente la próxima fecha

Próximamente: export a PDF, notificaciones push, plantillas por tipo de vivienda.
```

### 4.3 Estrategia de Screenshots

| # | Pantalla | Texto Overlay | Propósito |
|---|---|---|---|
| 1 | Dashboard | "Todo tu hogar en un vistazo" | Primera impresión |
| 2 | Lista de intervenciones | "Historial completo" | Funcionalidad principal |
| 3 | Formulario de intervención | "Registra en segundos" | Facilidad de uso |
| 4 | Inventario | "Tu hogar bajo control" | Inventario + garantías |
| 5 | Recordatorios | "Nunca olvides un mantenimiento" | Sistema de recordatorios |
| 6 | Profesionales | "Tu agenda de confianza" | Directorio profesionales |

**Resoluciones necesarias:**

| Plataforma | Dispositivo | Resolución |
|---|---|---|
| Android Teléfono | Pixel 8 Pro | 1290 × 2796 |
| Android Tablet 7" | — | 1600 × 2560 |
| Android Tablet 10" | — | 1920 × 2560 |
| iPhone 6.7" | iPhone 15 Pro Max | 1290 × 2796 |
| iPhone 6.5" | iPhone 14 Plus | 1284 × 2778 |
| iPhone 5.5" | iPhone 8 Plus | 1242 × 2208 |
| iPad 12.9" | iPad Pro | 2048 × 2732 |

### 4.4 Icono de la App

**Estado actual:**
- iOS: Icono 1024×1024 configurado en `iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/`
- Android: Iconos en `composeApp/src/androidMain/res/mipmap-*/`

**Checklist:**
- [ ] Sin transparencia (iOS lo rechaza)
- [ ] Sin esquinas redondeadas (las aplica el sistema)
- [ ] Legible a 29×29 px (tamaño más pequeño)
- [ ] Coherente con paleta terracota del rediseño

### 4.5 Gestión de Versiones

**Esquema:** `MAJOR.MINOR.PATCH` (ej: 1.0.0, 1.1.0, 1.0.1)

**Archivos a mantener sincronizados:**

| Archivo | Campo | Valor Inicial |
|---|---|---|
| `composeApp/build.gradle.kts` | `versionCode` | 1 |
| `composeApp/build.gradle.kts` | `versionName` | "1.0.0" |
| `iosApp/Configuration/Config.xcconfig` | `CURRENT_PROJECT_VERSION` | 1 |
| `iosApp/Configuration/Config.xcconfig` | `MARKETING_VERSION` | 1.0.0 |

**Regla:** `versionCode` / `CURRENT_PROJECT_VERSION` debe incrementarse en cada release. Nunca reutilizar.

---

## 5. Cambios Técnicos Necesarios

### 5.1 Resumen

| Archivo | Acción | Cambios |
|---|---|---|
| `composeApp/build.gradle.kts` | **Modificar** | Signing config, ProGuard, minify, shrink, versionName |
| `composeApp/proguard-rules.pro` | **Crear** | Reglas R8/ProGuard para release |
| `composeApp/src/androidMain/AndroidManifest.xml` | **Modificar** | Añadir `INTERNET` permission |
| `iosApp/Configuration/Config.xcconfig` | **Modificar** | Añadir TEAM_ID, corregir bundle ID |
| `iosApp/iosApp.xcodeproj/project.pbxproj` | **Modificar** | Reducir deployment target de 18.2 a 16.0 |
| `iosApp/iosApp/Info.plist` | **Modificar** | Añadir NSPhotoLibraryUsageDescription |
| `iosApp/iosApp/PrivacyInfo.xcprivacy` | **Crear** | Privacy manifest (obligatorio) |

### 5.2 Detalle de Cambios

#### composeApp/build.gradle.kts

```diff
 defaultConfig {
     versionCode = 1
-    versionName = "1.0"
+    versionName = "1.0.0"
 }

+signingConfigs {
+    create("release") {
+        storeFile = file(
+            System.getProperty("user.home") +
+            "/.android-keystores/hogarfix-release.jks"
+        )
+        storePassword = System.getenv("HOGARFIX_KEYSTORE_PASSWORD") ?: ""
+        keyAlias = "hogarfix"
+        keyPassword = System.getenv("HOGARFIX_KEY_PASSWORD") ?: ""
+    }
+}

 buildTypes {
     getByName("release") {
-        isMinifyEnabled = false
+        isMinifyEnabled = true
+        isShrinkResources = true
+        proguardFiles(
+            getDefaultProguardFile("proguard-android-optimize.txt"),
+            "proguard-rules.pro"
+        )
+        signingConfig = signingConfigs.getByName("release")
     }
 }
```

#### iosApp/Configuration/Config.xcconfig

```diff
-TEAM_ID=
+TEAM_ID=XXXXXXXXXX

-PRODUCT_BUNDLE_IDENTIFIER=com.hogarfix.HogarFix$(TEAM_ID)
+PRODUCT_BUNDLE_IDENTIFIER=com.hogarfix.HogarFix

-MARKETING_VERSION=1.0
+MARKETING_VERSION=1.0.0
```

#### iosApp/iosApp.xcodeproj/project.pbxproj (2 ocurrencias)

```diff
-IPHONEOS_DEPLOYMENT_TARGET = 18.2;
+IPHONEOS_DEPLOYMENT_TARGET = 16.0;
```

---

## 6. Timeline y Fases

### Fase 0 — Preparación (1-2 días)

- [ ] Crear cuenta Google Play Developer (~25 USD)
- [ ] Crear cuenta Apple Developer Program (~99 USD)
- [ ] Esperar aprobación de cuentas (24-48h)
- [ ] Diseñar icono final de la app
- [ ] Escribir y publicar política de privacidad

### Fase 1 — Configuración Técnica Android (1 día)

- [ ] Generar keystore de release
- [ ] Configurar signing en `build.gradle.kts`
- [ ] Crear `proguard-rules.pro`
- [ ] Añadir permiso INTERNET al manifest
- [ ] Activar minificación y shrink de recursos
- [ ] Build release y verificar que funciona en dispositivo físico

### Fase 2 — Configuración Técnica iOS (1 día)

- [ ] Configurar TEAM_ID en `Config.xcconfig`
- [ ] Corregir bundle identifier (quitar `$(TEAM_ID)`)
- [ ] Reducir deployment target a iOS 16.0
- [ ] Crear `PrivacyInfo.xcprivacy`
- [ ] Añadir `NSPhotoLibraryUsageDescription` a `Info.plist`
- [ ] Archive y verificar que compila

### Fase 3 — Preparación de Assets (2-3 días)

- [ ] Tomar screenshots en Android (teléfono + tablets)
- [ ] Tomar screenshots en iOS (iPhone + iPad)
- [ ] Añadir overlays de texto a screenshots
- [ ] Crear feature graphic 1024×500 (Android)
- [ ] Preparar vídeo promocional (opcional)
- [ ] Redactar descripción larga y corta

### Fase 4 — Configuración de Tiendas (1 día)

- [ ] Crear app en Google Play Console, rellenar ficha y data safety
- [ ] Crear app en App Store Connect, rellenar ficha y privacy labels

### Fase 5 — Testing (1-2 semanas)

- [ ] Subir AAB a testing interno (Google Play)
- [ ] Subir build a TestFlight (App Store)
- [ ] Reclutar 5-10 testers
- [ ] Recopilar feedback y corregir bugs críticos
- [ ] Iterar con nuevas builds si es necesario

### Fase 6 — Lanzamiento (2-3 días)

- [ ] Subir build final a producción (Android, rollout gradual)
- [ ] Enviar a revisión (iOS)
- [ ] Responder a posibles rechazos de Apple
- [ ] Verificar que ambas versiones están publicadas y funcionan

### Resumen de tiempos

| Fase | Duración |
|---|---|
| Preparación | 1-2 días |
| Config Android | 1 día |
| Config iOS | 1 día |
| Assets | 2-3 días |
| Tiendas | 1 día |
| Testing | 1-2 semanas |
| Lanzamiento | 2-3 días |
| **Total estimado** | **3-4 semanas** |

---

## 7. Checklist Pre-Lanzamiento

### 7.1 Código

- [ ] Versión 1.0.0 en todos los archivos de configuración
- [ ] `versionCode` / `CURRENT_PROJECT_VERSION` = 1
- [ ] ProGuard activado y build release probado (Android)
- [ ] App firmada con keystore de release
- [ ] Keystore respaldado en lugar seguro
- [ ] Build release probado en dispositivo físico Android
- [ ] Archive de Xcode probado en dispositivo físico iOS
- [ ] Sin crashes en cold start
- [ ] Sin ANRs ni memory leaks evidentes

### 7.2 Funcionalidad

- [ ] CRUD de intervenciones funciona correctamente
- [ ] CRUD de inventario (HomeItems) funciona correctamente
- [ ] CRUD de profesionales funciona correctamente
- [ ] CRUD de recordatorios funciona correctamente
- [ ] Completar recordatorio recalcula próxima fecha
- [ ] Dialog "Programar recordatorio?" tras nueva intervención funciona
- [ ] Dashboard muestra gastos, vencidos y próximos mantenimientos
- [ ] Photo picker funciona (añadir/ver foto)
- [ ] Filtros de categoría funcionan en todas las listas
- [ ] Swipe-to-delete funciona en todas las listas
- [ ] Llamar a profesional desde tarjeta funciona
- [ ] Dark mode funciona
- [ ] Rotación de pantalla no pierde datos
- [ ] Cierre y reapertura de app mantiene datos (persistencia Room)

### 7.3 Tiendas

- [ ] Icono subido y aprobado
- [ ] Screenshots en todos los tamaños requeridos
- [ ] Descripción breve (≤80 chars Android, ≤30 chars iOS subtitle)
- [ ] Descripción larga completa
- [ ] Categoría correcta seleccionada
- [ ] Clasificación por edad configurada
- [ ] URL de política de privacidad válida y accesible
- [ ] Sección de datos/privacidad completada

### 7.4 Legal / Compliance

- [ ] Política de privacidad publicada
- [ ] `PrivacyInfo.xcprivacy` incluido en el bundle iOS
- [ ] `NSPhotoLibraryUsageDescription` en `Info.plist`
- [ ] Permiso `INTERNET` en `AndroidManifest.xml`
- [ ] Sin uso de APIs restringidas no declaradas

### 7.5 Post-Lanzamiento

- [ ] Monitorizar crash reports (Xcode Organizer / Android Vitals)
- [ ] Responder a reviews en primeras 48h
- [ ] Tener preparado un hotfix por si hay bugs críticos
- [ ] Planificar versión 1.1.0 con feedback de usuarios

---

## 8. Apéndice: Comandos Útiles

### Android

```bash
# Limpiar proyecto
./gradlew clean

# Build debug APK
./gradlew :composeApp:assembleDebug

# Build release AAB (requiere signing config + env vars)
./gradlew :composeApp:bundleRelease

# Instalar debug en dispositivo conectado
./gradlew :composeApp:installDebug

# Verificar AAB firmado
jarsigner -verify -verbose \
  composeApp/build/outputs/bundle/release/composeApp-release.aab

# Ver info del keystore
keytool -list -v -keystore ~/.android-keystores/hogarfix-release.jks
```

### iOS

```bash
# Compilar framework Kotlin para release
./gradlew :composeApp:assembleReleaseXCFramework

# Abrir proyecto en Xcode
open iosApp/iosApp.xcodeproj

# Limpiar desde terminal
xcodebuild clean -project iosApp/iosApp.xcodeproj -scheme iosApp

# Archive desde terminal (alternativa a Xcode UI)
xcodebuild archive \
  -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -archivePath build/HogarFix.xcarchive
```

---

*Documento creado para HogarFix v1.0.0*
*Basado en requisitos de Google Play y App Store a febrero 2026*
