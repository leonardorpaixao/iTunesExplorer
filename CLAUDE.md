# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

iTunes Explorer is a Kotlin Multiplatform (KMP) application built with Compose Multiplatform that allows exploring iTunes Store content across Android, iOS, Web (WASM), and Desktop (JVM) platforms.

## Build & Run Commands

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
```bash
./gradlew :composeApp:iosSimulatorArm64Binaries
```
Note: After building, open the iOS project in Xcode to run on simulator.

### Web (WASM)
```bash
./gradlew :composeApp:wasmJsBrowserRun
```

### Desktop (JVM)
```bash
./gradlew :composeApp:run
```

### Clean Build
```bash
./gradlew clean
```

## Module Architecture

The project follows a modular architecture with clear separation of concerns:

```
iTunesExplorer/
├── composeApp/          # Main application module
├── core/
│   ├── network/         # Network layer and API
│   ├── error/           # Error handling
│   └── common/          # Common utilities
├── design-system/       # Reusable UI components
└── features/
    └── listing/         # Listing and search screen
```

### Core Modules (`core/`)
- **`core:network`** - Network layer using Ktorfit (type-safe Ktor wrapper) for the iTunes Search API
  - Platform-specific HTTP clients: OkHttp (Android), Darwin (iOS), JS (WASM)
  - Ktorfit requires KSP for code generation
  - Base URL: `https://itunes.apple.com/`
  - Main endpoints: `/search` and `/lookup`

- **`core:error`** - Centralized error handling

- **`core:common`** - Shared utilities and extensions

### Design System (`design-system/`)
- Reusable UI components and theming
- Material3-based design system
- Platform-agnostic Compose components

### Features (`features/`)
Feature modules follow a presentation layer pattern with Voyager:

- **`features:listing`** - Search and browse iTunes content
  - Uses `StateScreenModel` for state management
  - Supports media type filtering
  - Integrates directly with `ITunesApi`
  - Single feature module containing all UI screens

### Main App (`composeApp/`)
- Platform-specific entry points (Android, iOS, WASM, Desktop)
- Dependency injection setup via Koin
- Voyager Navigator with SlideTransition for navigation
- Desktop main class: `com.itunesexplorer.MainKt`

## Key Architectural Patterns

### Dependency Injection
- **Kodein-DI** is used for DI across all platforms
- DI setup in `composeApp/src/commonMain/kotlin/com/itunesexplorer/di/AppModule.kt`
- Modules are imported: `networkModule`, `listingModule`
- Each feature module provides its own Kodein module
- Compose integration via `org.kodein.di.compose.withDI`

### Navigation
- **Voyager** handles multiplatform navigation
- Screens extend Voyager's `Screen` interface
- ScreenModels extend `StateScreenModel` for state management
- Navigator initialized in `App.kt` with `ListingScreen` as the starting point

### Network Layer
- **Ktorfit** provides type-safe API definitions
- Interface-based API declarations with annotations (`@GET`, `@Query`)
- Platform-specific Ktor clients configured in `networkModule`
- JSON serialization with Kotlinx Serialization
- Configuration: lenient parsing, ignore unknown keys, 30s timeout

### State Management
- Feature screens use `StateScreenModel` from Voyager
- State flows with immutable data classes
- Coroutines for async operations via `screenModelScope`

## Important Technical Details

### Build Configuration
- **Kotlin**: 2.0.0
- **Compose Multiplatform**: 1.7.0
- **Target JVM**: Java 11
- **Android**: minSdk 24, targetSdk 34
- **iOS Framework**: Static framework named "ComposeApp"

### KSP Code Generation
The `core:network` module uses KSP for Ktorfit code generation. KSP configurations are required for each platform target:
- `kspCommonMainMetadata`
- `kspAndroid`
- `kspIosX64`, `kspIosArm64`, `kspIosSimulatorArm64`
- `kspWasmJs`

When adding new Ktorfit interfaces, ensure KSP is properly configured.

### Platform-Specific Dependencies
- Android: Activity Compose, Koin Android
- Desktop/iOS/WASM: Coroutines Core
- Each platform has its own Ktor client engine

## Development Workflow

### Adding New Features
1. Create feature module in `features/`
2. Define Kodein module for DI using `DI.Module("moduleName")`
3. Create Voyager Screen and StateScreenModel
4. Add module dependency in `composeApp/build.gradle.kts`
5. Import Kodein module in `appDI` using `importAll()`
6. Use `rememberScreenModel { ScreenModel(it.instance()) }` in screens to get dependencies

### Modifying Network Layer
1. Update API interface in `core:network/src/commonMain/.../ITunesApi.kt`
2. Add/modify data models in `ITunesModels.kt`
3. KSP will regenerate code on next build

### iTunes Search API
- Public API, no authentication required
- Documentation: https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html
- Default parameters: limit=50, country=US, lang=en_us

## Build System Notes

### Gradle Configuration
- Gradle wrapper is configured for version 8.10
- Uses Gradle version catalogs (libs.versions.toml) for dependency management
- **Configuration cache is DISABLED** due to compatibility issues with Kotlin Native iOS targets
  - Android Studio sync fails with `commonizeNativeDistribution` task creation error when configuration cache is enabled
  - Workaround: `org.gradle.configuration-cache=false` in `gradle.properties`
- C-interop commonization is disabled (`kotlin.mpp.enableCInteropCommonization=false`) to prevent task creation conflicts

### Known Limitations
- **WASM Support**:
  - Ktorfit 2.1.0 does not support WASM targets yet. WASM has been disabled in `core:network` module
  - Voyager-Kodein integration is not available for WASM. Feature modules use platform-specific source sets to include Kodein only for non-WASM targets
  - To re-enable WASM: uncomment WASM targets in all modules, update Ktorfit when support is available, and restructure dependency injection for WASM
- **Ktorfit KSP**: The KSP artifact version must include the KSP compatibility suffix (e.g., `2.1.0-1.0.27` where 1.0.27 is the KSP version)
- **Voyager Version**: Using 1.1.0-beta03 which includes Kodein integration support
- **Kodein-DI Version**: Using 7.22.0 with Compose framework integration

### Common Issues & Solutions

1. **Android Studio Sync Error with iOS Targets**:
   - **Error**: `Could not create task ':commonizeNativeDistribution'. The value for task ':commonizeNativeDistribution' property 'kotlinNativeBundleBuildService' cannot be changed any further.`
   - **Root Cause**: Gradle configuration cache incompatibility with Kotlin Multiplatform iOS native target commonization
   - **Solution**: Configuration cache and C-interop commonization have been disabled in `gradle.properties`:
     ```properties
     org.gradle.configuration-cache=false
     kotlin.mpp.enableCInteropCommonization=false
     kotlin.native.cacheKind=none
     ```
   - If sync still fails:
     1. Stop Gradle daemons: `./gradlew --stop`
     2. Delete caches: `rm -rf .gradle .kotlin build ~/.gradle/caches/configuration-cache`
     3. Re-sync in Android Studio

2. **Multiplatform String Formatting**:
   - **Issue**: `String.format()` is not available in KMP common code
   - **Solution**: Use platform-agnostic string operations like `padStart()`, string interpolation, or expect/actual declarations

3. **Missing Compose Compiler Plugin**:
   - Kotlin 2.0+ requires `org.jetbrains.kotlin.plugin.compose` to be applied alongside `org.jetbrains.compose`

4. **JVM Target Mismatch**:
   - All Android library modules need explicit JVM target configuration:
   ```kotlin
   androidTarget {
       compilations.all {
           kotlinOptions {
               jvmTarget = "11"
           }
       }
   }
   ```

5. **Android Library Config**:
   - All KMP modules with `androidTarget()` need:
     - `com.android.library` plugin applied
     - Android configuration block with namespace, compileSdk, minSdk, and compileOptions
