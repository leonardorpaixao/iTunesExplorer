# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

iTunes Explorer is a Kotlin Multiplatform (KMP) application built with Compose Multiplatform that allows exploring iTunes Store content across Android, iOS, and Desktop (JVM) platforms.

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
│   ├── error/           # Error handling
│   ├── common/          # Common utilities and MVI base classes
│   ├── settings/        # User settings and preferences
│   └── currency/        # Currency formatting utilities
├── design-system/       # Reusable UI components
└── features/
    ├── home/            # Home screen with bottom navigation
    ├── catalog/         # iTunes catalog browsing and search
    └── preferences/     # User preferences and settings
```

### Core Modules (`core/`)
- **`core:error`** - Centralized error handling
  - `DomainError`: Sealed class for domain errors
  - `runCatchingDomain`: Extension for error handling

- **`core:common`** - Shared utilities, extensions, and MVI base classes
  - `MviViewModel<State, Intent, Effect>`: Base class for all ViewModels
  - `ViewState`, `ViewIntent`, `ViewEffect`: MVI marker interfaces
  - Effect channel for one-time side effects

- **`core:settings`** - User settings management
  - Country and language preferences
  - Settings persistence

- **`core:currency`** - Currency formatting utilities
  - Locale-aware currency formatting

### Design System (`design-system/`)
- Reusable UI components and theming
- Material3-based design system
- Platform-agnostic Compose components

### Features (`features/`)
Feature modules follow **MVI (Model-View-Intent)** pattern with Voyager:

- **`features:home`** - Home screen with bottom navigation tabs
  - **HomeScreenModel**: Manages tab selection (Álbuns, Pesquisa, Preferências)
    - State: `selectedTab`
    - Intent: `SelectTab`
  - **UI Structure**:
    - TopAppBar with app name (clickable to return to Albums tab)
    - Bottom navigation with 3 tabs
    - Content area with tab-specific screens

- **`features:catalog`** - iTunes catalog browsing and search
  - **Network Layer** (internal):
    - `ITunesApi`: API interface for iTunes Search API
    - `ITunesApiImpl`: Ktor-based implementation
    - Platform-specific HTTP clients: OkHttp (Android), Darwin (iOS)
    - Base URL: `https://itunes.apple.com/`
    - Main endpoints: `/search` and `/lookup`
  - **Domain Layer**:
    - `MediaType`: Enum for media types (music, podcast, etc.)
    - `MusicGenre`: Enum for music genres
    - Repository interfaces: `SearchRepository`, `AlbumsRepository`, `DetailsRepository`
  - **Presentation Layer**:
    - **AlbumsTabModel**: Displays top album recommendations
      - State: `recommendations`, `isLoading`, `error`, `selectedGenre`
      - Intent: `LoadRecommendations`, `SelectGenre`, `Retry`
      - Effect: `ShowError`
    - **SearchTabModel**: Text search with MediaType filters
      - State: `items`, `searchQuery`, `selectedMediaType`, `isLoading`, `error`, `showRegionHint`
      - Intent: `UpdateSearchQuery`, `Search`, `SelectMediaType`, `Retry`
      - Effect: `ShowError`
    - **DetailsScreenModel**: Item details view
      - State: `item`, `relatedItems`, `isLoading`, `error`
      - Intent: `Retry`
      - Effect: `ShowError`

- **`features:preferences`** - User preferences and settings
  - Country and language selection
  - Settings persistence

### Main App (`composeApp/`)
- Platform-specific entry points (Android, iOS, Desktop)
- Dependency injection setup via Kodein
- Voyager Navigator with SlideTransition for navigation
- Desktop main class: `com.itunesexplorer.MainKt`

## Key Architectural Patterns

### Dependency Injection
- **Kodein-DI** is used for DI across all platforms
- DI setup in `composeApp/src/commonMain/kotlin/com/itunesexplorer/di/AppModule.kt`
- Modules are imported: `settingsModule`, `currencyModule`, `homeModule`
- Each feature module provides its own Kodein module
- `catalogModule` is imported by `homeModule` (network setup included)
- Compose integration via `org.kodein.di.compose.withDI`

### Navigation
- **Voyager** handles multiplatform navigation
- Screens extend Voyager's `Screen` interface
- ScreenModels extend `StateScreenModel` for state management
- Navigator initialized in `App.kt` with `HomeScreen` as the starting point

### Network Layer
- **Ktorfit** provides type-safe API definitions
- Interface-based API declarations with annotations (`@GET`, `@Query`)
- Platform-specific Ktor clients configured in `networkModule`
- JSON serialization with Kotlinx Serialization
- Configuration: lenient parsing, ignore unknown keys, 30s timeout

### State Management - MVI Pattern
The project implements **MVI (Model-View-Intent)** for predictable, unidirectional data flow:

**Core Concepts**:
- **ViewState**: Immutable data classes representing complete UI state
- **ViewIntent**: Sealed classes/interfaces representing user intentions
- **ViewEffect**: Sealed classes for one-time side effects (toasts, navigation)
- **MviViewModel**: Base class extending `StateScreenModel` with effect channel

**Implementation Pattern**:
```kotlin
// State - What the UI needs to know
data class MyViewState(
    val data: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) : ViewState

// Intent - What the user can do
sealed class MyIntent : ViewIntent {
    data object LoadData : MyIntent()
    data class UpdateQuery(val query: String) : MyIntent()
}

// Effect - One-time events
sealed class MyEffect : ViewEffect {
    data class ShowError(val message: String) : MyEffect()
}

// ViewModel
class MyScreenModel(api: Api) : MviViewModel<MyViewState, MyIntent, MyEffect>(
    initialState = MyViewState()
) {
    override fun onAction(intent: MyIntent) {
        when (intent) {
            is MyIntent.LoadData -> loadData()
            is MyIntent.UpdateQuery -> updateQuery(intent.query)
        }
    }

    private fun loadData() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true) }
            try {
                val data = api.fetchData()
                mutableState.update { it.copy(data = data, isLoading = false) }
            } catch (e: Exception) {
                mutableState.update { it.copy(error = e.message, isLoading = false) }
                sendEffect(MyEffect.ShowError(e.message ?: "Error"))
            }
        }
    }
}
```

**Benefits**:
- Unidirectional data flow (View → Intent → Model → View)
- Immutable state for predictability
- Easy to test (each Intent produces deterministic output)
- Clear separation of concerns
- Side effects handled separately from state

See full documentation in `docs/MVI_ARCHITECTURE.md`

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

When adding new Ktorfit interfaces, ensure KSP is properly configured.

### Platform-Specific Dependencies
- Android: Activity Compose, Kodein Android
- Desktop/iOS: Coroutines Core
- Each platform has its own Ktor client engine

## Development Workflow

### Adding New Features (MVI Pattern)
1. Create feature module in `features/`
2. Define ViewState, ViewIntent, and ViewEffect for the feature:
   ```kotlin
   data class MyViewState(...) : ViewState
   sealed class MyIntent : ViewIntent { ... }
   sealed class MyEffect : ViewEffect { ... }
   ```
3. Create ViewModel extending `MviViewModel`:
   ```kotlin
   class MyScreenModel(deps) : MviViewModel<MyViewState, MyIntent, MyEffect>(
       initialState = MyViewState()
   ) {
       override fun onAction(intent: MyIntent) { ... }
   }
   ```
4. Create Voyager Screen with `rememberInstance()` for DI
5. Define Kodein module for DI using `DI.Module("moduleName")`
6. Add module dependency in `composeApp/build.gradle.kts`
7. Import Kodein module in `appDI` using `importAll()`
8. In the UI, use `onAction()` for all user actions:
   ```kotlin
   Button(onClick = { screenModel.onAction(MyIntent.DoAction) })
   ```

### Modifying iTunes API
1. Update API interface in `features/catalog/src/commonMain/kotlin/com/itunesexplorer/catalog/data/api/ITunesApi.kt`
2. Add/modify data models in `features/catalog/src/commonMain/kotlin/com/itunesexplorer/catalog/data/api/ITunesModels.kt`
3. Update mappers in `features/catalog/src/commonMain/kotlin/com/itunesexplorer/catalog/data/mapper/` to handle new fields
4. All API classes are marked `internal` - only domain models should be public

### iTunes Search API
- Public API, no authentication required
- Documentation: https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html
- Default parameters: limit=50, country=US, lang=en_us

#### API Limitations
**⚠️ Important**: The iTunes Search API has the following limitations:
- **No pagination support**: The API does not provide `offset` or `page` parameters
- **Maximum results**: 200 items per request (using `limit` parameter, range: 1-200)
- **No cursor/token**: No mechanism to retrieve results beyond the first 200 items
- **Workarounds**:
  - Use more specific search terms to narrow results
  - Adjust `limit` parameter (up to 200) to get more results per request
  - Implement client-side filtering/sorting on received results
  - Use different `media` or `entity` filters to segment searches

### Testing ViewModels (MVI Pattern)
All ViewModels have comprehensive unit tests using:
- **Kotlin Test**: Standard testing framework
- **Kotlinx Coroutines Test**: `StandardTestDispatcher`, `runTest`, `advanceUntilIdle()`
- **Turbine**: Flow testing library for state/effect assertions

**Test Structure**:
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MyScreenModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeApi: FakeApi

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeApi = FakeApi()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load data successfully on init`() = runTest(testDispatcher) {
        val viewModel = MyScreenModel(fakeApi)
        advanceUntilIdle() // Complete async initialization

        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.items.size)
        }
    }

    @Test
    fun `onAction should update state`() = runTest(testDispatcher) {
        val viewModel = MyScreenModel(fakeApi)
        advanceUntilIdle()

        viewModel.state.test {
            awaitItem() // current state

            viewModel.onAction(MyIntent.UpdateQuery("test"))

            val state = awaitItem()
            assertEquals("test", state.query)
        }
    }
}
```

**Key Points**:
- Create ViewModels **inside** `runTest(testDispatcher)` blocks
- Call `advanceUntilIdle()` after async operations
- Use Turbine's `test {}` for Flow assertions
- Implement Fake APIs with controllable behavior (`shouldFail` flags)
- One concept per test

**Running Tests**:
```bash
# Run all tests for home feature
./gradlew :features:home:testDebugUnitTest

# View HTML report
open features/home/build/reports/tests/testDebugUnitTest/index.html
```

**Current Test Coverage**:
- ✅ HomeScreenModel: 4 tests
- ✅ AlbumsTabModel: 4 tests
- ✅ SearchTabModel: 9 tests
- ✅ PreferencesTabModel: 2 tests
- **Total**: 19 tests, 100% passing

See full testing guide in `docs/TESTING.md`

## Build System Notes

### Gradle Configuration
- Gradle wrapper is configured for version 8.10
- Uses Gradle version catalogs (libs.versions.toml) for dependency management
- **Configuration cache is DISABLED** due to compatibility issues with Kotlin Native iOS targets
  - Android Studio sync fails with `commonizeNativeDistribution` task creation error when configuration cache is enabled
  - Workaround: `org.gradle.configuration-cache=false` in `gradle.properties`
- C-interop commonization is disabled (`kotlin.mpp.enableCInteropCommonization=false`) to prevent task creation conflicts

### Known Limitations
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
- sempre execute a build de mobile como padrão