# iTunes Explorer

> A production-ready Kotlin Multiplatform application demonstrating modern mobile architecture and cross-platform development expertise

![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-7F52FF?style=flat&logo=kotlin)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.1-4285F4?style=flat)
![Platforms](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20Desktop-success)
![Tests](https://img.shields.io/badge/Tests-87%20passing-brightgreen)

**iTunes Explorer** is a fully-functional multiplatform application that allows users to browse and search the iTunes Store catalog across Android, iOS, and Desktop platforms. Built with Kotlin Multiplatform and Compose Multiplatform, it showcases **95%+ code sharing** across all three platforms while maintaining native performance and platform-specific optimizations.

## 📖 About This Project

I built iTunes Explorer to deepen my expertise in Kotlin Multiplatform and modern mobile architecture patterns. The project demonstrates:

- **Production-ready architecture**: Full implementation of the MVI (Model-View-Intent) pattern with comprehensive unit testing
- **Platform expertise**: Platform-specific HTTP client implementations addressing real-world challenges (iOS Content-Length workarounds, Android OkHttp optimization)
- **Clean code principles**: Modular architecture with clear separation of concerns, dependency injection, and testable design
- **Professional development practices**: Type-safe error handling, structured logging, internationalization support, and extensive documentation

Through this project, I gained hands-on experience solving complex multiplatform challenges, from Gradle configuration issues to platform-specific networking quirks, while maintaining clean architecture principles.

## 📸 Screenshots

> **TODO**: Add screenshots below once captured

### Desktop (macOS)
![Desktop Screenshot Placeholder](docs/screenshots/desktop.png)
*Albums tab showing genre-filtered recommendations*

### Android
![Android Screenshot Placeholder](docs/screenshots/android.png)
*Search functionality with MediaType filters*

### iOS
![iOS Screenshot Placeholder](docs/screenshots/ios.png)
*Album details screen with artwork and metadata*

### Platform Comparison
![Platform Comparison Placeholder](docs/screenshots/platforms.png)
![img.png](docs/comparison.png)
*Side-by-side view of the same screen across all three platforms*

**How to capture screenshots**:
1. Run the app on each platform
2. Navigate to key screens (Albums, Search, Details)
3. Save to `docs/screenshots/` directory
4. Update image paths above

## ✨ Features

### 🎵 Albums Tab
- Browse top album recommendations from iTunes Store
- Filter by music genre (Rock, Pop, Hip-Hop, Electronic, Jazz, Classical, Country, R&B/Soul)
- Click any album to view detailed information
- Responsive grid layout adapted for each platform

### 🔍 Search Tab
- Real-time text search across iTunes catalog
- Filter results by MediaType (Music, Movies, Podcasts, TV Shows, Apps, Audiobooks, eBooks)
- Dynamic result updates as filters are applied
- Region-aware search with helpful hints
- Handles empty states and error scenarios gracefully

### 📱 Details Screen
- Full album/item information with high-resolution artwork
- Localized release dates and currency formatting
- Direct links to iTunes Store
- Related items suggestions
- Platform-optimized image loading with Coil

### ⚙️ Preferences Tab
- Country selection for localized content
- Language preferences
- Persistent settings across app restarts
- Settings synchronized across all features

### 🔧 Technical Features
- **MVI Architecture**: Unidirectional data flow with predictable state management
- **Dependency Injection**: Kodein-DI for clean, testable code
- **Comprehensive Testing**: 87 unit tests across all layers with 100% pass rate
- **Multiplatform Logging**: Platform-specific implementations (Logcat, NSLog, Console)
- **Type-Safe Error Handling**: Domain-specific error types with proper propagation
- **Internationalization**: Country/language support with localized formatting

## 🛠️ Technologies

### Core Technologies

| Category | Technology | Version | Purpose |
|----------|-----------|---------|---------|
| **Language** | Kotlin | 2.1.10 | Primary development language |
| **Framework** | Kotlin Multiplatform | 2.1.10 | Cross-platform code sharing |
| **UI Framework** | Compose Multiplatform | 1.7.1 | Declarative UI across platforms |
| **Build System** | Gradle | 8.10 | Build automation with version catalogs |

### Libraries

| Library | Version | Purpose |
|---------|---------|---------|
| **Voyager** | 1.1.0-beta02 | Multiplatform navigation with screen models |
| **Kodein-DI** | 7.21.2 | Dependency injection framework |
| **Kotlinx Coroutines** | 1.9.0 | Asynchronous programming |
| **Ktor Client** | 3.0.2 | HTTP client with platform-specific engines |
| **Ktor OkHttp** | 3.0.2 | Android HTTP engine (optimized) |
| **Ktor Darwin** | 3.0.2 | iOS HTTP engine (native) |
| **Ktor CIO** | 3.0.2 | Desktop HTTP engine (pure Kotlin) |
| **Kotlinx Serialization** | 1.7.1 | JSON serialization/deserialization |
| **Material3** | (via Compose) | Material Design 3 components |
| **Coil** | 3.0.4 | Async image loading for all platforms |
| **Lyricist** | 1.7.0 | Internationalization (i18n) support |

### Testing

| Library | Version | Purpose |
|---------|---------|---------|
| **Kotlin Test** | 2.1.10 | Multiplatform unit testing |
| **Kotlinx Coroutines Test** | 1.9.0 | Coroutine testing utilities |
| **Turbine** | 1.1.0 | Flow testing library |

### Platform-Specific

| Platform | Technologies |
|----------|-------------|
| **Android** | Activity Compose, Kodein Android extensions |
| **iOS** | Darwin native APIs, NSLog logging |
| **Desktop** | Java 11+, Swing integration |

## 📂 Project Structure

```
.
├── composeApp/
│   ├── src/
│   │   ├── commonMain/          # Shared app logic
│   │   ├── androidMain/         # Android entry point
│   │   ├── iosMain/             # iOS entry point
│   │   └── desktopMain/         # Desktop entry point
│   └── build.gradle.kts
├── foundation/                  # MVI framework, extensions, i18n
│   └── src/
│       ├── commonMain/          # MviViewModel, extensions
│       ├── androidMain/         # Android-specific utilities
│       ├── iosMain/             # iOS-specific utilities
│       └── desktopMain/         # Desktop-specific utilities
├── core/
│   ├── error/                   # Error definitions
│   ├── logger/                  # Multiplatform logging
│   ├── network/                 # HTTP client setup
│   ├── settings/                # User preferences
│   └── currency/                # Currency definitions
├── design-system/               # UI components
├── features/
│   ├── home/                    # App Shell to hold tabs
│   ├── catalog/                 # iTunes integration + tests
│   └── preferences/             # Language and Country prefs + tests
├── iosApp/                      # Xcode project
├── docs/                        # Documentation
├── gradle/                      # Gradle wrapper & catalogs
└── README.md                    # This file
```

### Modules Dependencies

The following diagram shows the dependency graph between modules:

![dependencies_graph.png](docs/dependencies_graph.png)
**Dependency Rules**:
- **Base layer**: `core:error`, `core:logger`, and `core:settings` have no dependencies on other modules
- **Foundation layer**: Provides MVI framework and common utilities, depends only on base core modules (`core:error`, `core:logger`)
- **Network layer**: `core:network` depends on `core:error` and `core:logger` for error handling and logging
- **Currency layer**: `core:currency` is independent (no module dependencies)
- **Features**: Depend on `foundation` and core modules as needed, never the reverse
- **Cross-feature**: Features can depend on other features (home → catalog)
- **Platform-specific**: Uses `expect/actual` declarations where needed

## 🏗️ Feature Module Architecture

The project implements **MVI (Model-View-Intent)** pattern for predictable, unidirectional data flow:

```
┌─────────┐      Intent      ┌───────────┐
│  View   │ ──────────────>  │ ViewModel │
│         │                  │   (MVI)   │
└─────────┘                  └───────────┘
     ▲                             │
     │    State And Effects        │
     └─────────────────────────────┘
```

**Key Concepts**:
- **ViewState**: Immutable data classes representing complete UI state
- **ViewIntent**: Sealed classes representing user intentions
- **ViewEffect**: One-time side effects (toasts, navigation)
- **MviViewModel**: Base class with automatic logging and effect handling

An instance of the architecture used on a feature module:

![catalog_graph.png](docs/catalog_graph.png)

## 🚀 Getting Started

### Prerequisites

- **JDK 11+** (recommended: JDK 17)
- **Android Studio** (latest stable version)
- **Xcode 15+** (for iOS builds, macOS only)
- **Gradle 8.10** (included via wrapper)
```

### Quick Start

#### Desktop (Fastest)
```bash
./gradlew :composeApp:runDesktop
```

#### Android
```bash
# Install and run on connected device/emulator
./gradlew :composeApp:runAndroid

# Or build APK only
./gradlew :composeApp:assembleDebug
```

#### iOS
```bash
# 1. Build Kotlin framework for simulator
./gradlew :composeApp:linkDebugFrameworkIosArm64

# 2. Open Xcode project
open iosApp/iosApp.xcodeproj

# 3. Select iPhone simulator and press Run (Cmd+R)
```

### Available Gradle Tasks

#### Application Tasks
```bash
# Run Desktop application (fastest way to test)
./gradlew :composeApp:runDesktop

# Install and run on Android device/emulator
./gradlew :composeApp:runAndroid
```

#### iOS Build Tasks
```bash
# Build for iOS Simulator (Apple Silicon Macs)
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

# Build for iOS Simulator (Intel Macs)
./gradlew :composeApp:linkDebugFrameworkIosX64

# Build for physical iOS device
./gradlew :composeApp:linkDebugFrameworkIosArm64

# Release builds
./gradlew :composeApp:linkReleaseFrameworkIosSimulatorArm64
./gradlew :composeApp:linkReleaseFrameworkIosArm64
```

#### Testing Tasks
```bash
# Run all tests across all modules
./gradlew allTests

# Run tests for specific target
./gradlew testDebugUnitTest          # Android tests
./gradlew desktopTest                # Desktop tests
./gradlew iosSimulatorArm64Test      # iOS simulator tests

# Run tests for specific module
./gradlew :features:catalog:testDebugUnitTest
./gradlew :features:home:testDebugUnitTest
./gradlew :features:preferences:testDebugUnitTest
./gradlew :core:currency:testDebugUnitTest
./gradlew :core:settings:testDebugUnitTest
```

#### Build & Verification
```bash
# Clean all build artifacts
./gradlew clean

# Run all checks (tests + linting)
./gradlew check

# Build everything
./gradlew build

# Run Android lint
./gradlew lint
```

## 🧪 Testing

The project has **comprehensive test coverage** with modern Kotlin testing tools:

- **Kotlin Test**: Standard multiplatform testing framework
- **Kotlinx Coroutines Test**: `StandardTestDispatcher`, `runTest`, `advanceUntilIdle()`
- **Turbine**: Flow testing library for state and effect assertions

### Test Coverage

**Presentation Layer** - 38 tests:
- ✅ **HomeScreenModel**: 4 tests (tab selection, navigation)
- ✅ **AlbumsTabModel**: 8 tests (loading, genre filtering, error handling, retry)
- ✅ **SearchTabModel**: 11 tests (search functionality, media type filtering, region hints)
- ✅ **DetailsScreenModel**: 10 tests (item loading, retry logic, navigation effects)
- ✅ **PreferencesTabModel**: 5 tests (country selection, reactive updates)

**Domain/Data Layer** - 49 tests:
- ✅ **CountryManager**: 6 tests (country selection, persistence)
- ✅ **LanguageManager**: 6 tests (language selection, persistence)
- ✅ **CurrencyFormatter**: 21 tests (formatting, localization, edge cases, rounding)
- ✅ **SupportedCurrencies**: 16 tests (data class validation, lookup, zero-decimal currencies)

**Total**: **87 tests, 100% passing**

**Test Infrastructure:**
- Fake implementations for use cases and repositories
- Test fixtures for domain models
- Comprehensive async testing with coroutines
- Flow testing with Turbine for reactive state

### Running Tests

```bash
# Run all tests
./gradlew :features:home:testDebugUnitTest :features:catalog:testDebugUnitTest :features:preferences:testDebugUnitTest :core:currency:testDebugUnitTest :core:settings:testDebugUnitTest

# Run tests for a specific module
./gradlew :features:catalog:testDebugUnitTest

# View HTML test report
open features/catalog/build/reports/tests/testDebugUnitTest/index.html
```

📚 **Detailed Testing Guide**: See [docs/TESTING.md](docs/TESTING.md) for test implementation patterns

## 🎯 Technical Highlights

### Challenges Solved

**1. iOS HTTP Client Content-Length Issue**
- **Problem**: Darwin engine doesn't automatically set `Content-Length` header, causing API failures
- **Solution**: Implemented platform-specific workaround using `Accept-Encoding: identity` header

**2. Gradle Configuration Cache Incompatibility**
- **Problem**: Android Studio sync fails with iOS targets when configuration cache is enabled
- **Solution**: Disabled configuration cache and C-interop commonization in `gradle.properties`

**3. Multiplatform String Formatting**
- **Problem**: `String.format()` not available in Kotlin common code
- **Solution**: Used platform-agnostic operations (`padStart()`, string interpolation, expect/actual)

### Architectural Decisions

**MVI over MVVM**: Chose MVI for predictable state management and easier testing. All state changes are explicit and traceable.

**Modular Architecture**: Separated features into independent modules to enable parallel development, better build times, and clear boundaries.

**Platform-Specific HTTP Engines**: Used native engines (OkHttp, Darwin, CIO) for optimal performance on each platform rather than a one-size-fits-all approach.

**Type-Safe Error Handling**: Implemented `DomainError` sealed class to avoid string-based error messages and enable exhaustive when statements.

### Code Quality Practices

- Immutable data classes for all state
- Sealed classes for type-safe intent and error handling
- Extension functions for reusability
- Dependency injection for testability
- Comprehensive unit tests for business logic
- Platform-specific implementations only where necessary


## 🌐 API Reference

This project uses Apple's public **[iTunes Search API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html)**.

**Key Endpoints**:
- `/search`: Search iTunes Store content
- `/lookup`: Fetch item details by ID

**Default Parameters**: `limit=200`, `country=US`, `lang=en_us`

**Note**: The API has a maximum limit of 200 results per request with no pagination support.

---

**Built with ❤️ using Kotlin Multiplatform**
