# iTunes Explorer

A multiplatform application built with Kotlin Multiplatform (KMP) and Compose Multiplatform that allows exploring iTunes Store content.

## 🚀 Technologies

- **Kotlin Multiplatform (KMP)** - Code sharing across platforms
- **Compose Multiplatform** - Declarative UI for all platforms
- **Voyager** - Multiplatform navigation
- **Kodein** - Dependency injection
- **Ktor Client** - Multiplatform HTTP client
- **Coroutines** - Asynchronous programming

## 📱 Supported Platforms

- ✅ Android
- ✅ iOS
- ✅ Desktop (JVM)

## 🏗️ Architecture

The project follows a clean modular architecture with **MVI (Model-View-Intent)** pattern:

```
iTunesExplorer/
├── composeApp/          # Main application
├── core/
│   ├── error/           # Error handling
│   ├── common/          # Common utilities + MVI base classes
│   ├── settings/        # User settings and preferences
│   └── currency/        # Currency formatting utilities
├── design-system/       # Reusable UI components
└── features/
    ├── home/            # Home screen with bottom navigation
    ├── catalog/         # iTunes catalog browsing and search
    └── preferences/     # User preferences and settings
```

### MVI (Model-View-Intent)

The project uses the MVI pattern for state management:

- **ViewState**: Immutable UI state (data classes)
- **ViewIntent**: User intentions (sealed classes)
- **ViewEffect**: One-time side effects (toasts, navigation)
- **MviViewModel**: Base class for all ViewModels

See complete documentation at [docs/MVI_ARCHITECTURE.md](docs/MVI_ARCHITECTURE.md)

## 🎨 Features

### Albums Tab
- **Top Albums**: Recommendations of the most popular albums
- **Genre Filter**: Filter albums by music genre
- **Details Navigation**: Click on an album to see more information

### Search Tab
- **Text Search**: Search field to find specific content
- **MediaType Filters**: Chips to filter by Music, Movie, Podcast, App, etc.
- **Dynamic Results**: Real-time updates as filters are applied

### Preferences Tab
- **Country Selection**: Choose your preferred country for content
- **Language Selection**: Choose your preferred language
- **Settings Persistence**: Preferences are saved and applied across the app

### General
- **Responsive Interface**: UI adapted for each platform
- **Bottom Navigation**: Intuitive navigation between tabs
- **TopBar with Logo**: Click on app name to return to Albums tab

## 🔧 How to Run

### Desktop (JVM)
```bash
./gradlew :composeApp:runDesktop
```

### Android
```bash
# Install and run on connected device/emulator
./gradlew :composeApp:runAndroid

# Or just build the APK
./gradlew :composeApp:assembleDebug
```

### iOS
First, compile the Kotlin Multiplatform framework:
```bash
# Build for Simulator (arm64)
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

# Build for Device (arm64)
./gradlew :composeApp:linkDebugFrameworkIosArm64
```

After building, open the project in Xcode:
```bash
open iosApp/iosApp.xcodeproj
```

**Important**:
- The iOS project is already configured to use the ComposeApp framework
- Before running in Xcode, always compile the corresponding framework (Simulator or Device)
- To switch between Simulator and Device, you need to recompile the appropriate framework

## 📋 Available Gradle Tasks

| Task | Description |
|------|-------------|
| `runDesktop` | Runs the Desktop (JVM) app |
| `runAndroid` | Installs and runs the Android app on device/emulator |
| `buildIosSimulator` | Compiles the iOS framework for simulator |
| `buildIosDevice` | Compiles the iOS framework for device |

## 📦 Main Dependencies

- Compose Multiplatform 1.7.0
- Kotlin 2.0.0
- Voyager 1.1.0-beta03
- Kodein 7.22.0
- Ktor 3.0.0
- Kotlinx Serialization 1.7.1
- Kotlinx Coroutines 1.9.0

## 🔑 API

The app uses Apple's public [iTunes Search API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html).

## 🧪 Testing

The project has comprehensive unit tests for all ViewModels using:

- **Kotlin Test**: Standard testing framework
- **Kotlinx Coroutines Test**: Asynchronous code testing
- **Turbine**: Flow testing

### Running Tests

```bash
# Run all tests for the home module
./gradlew :features:home:testDebugUnitTest

# View HTML report
open features/home/build/reports/tests/testDebugUnitTest/index.html
```

### Coverage

- ✅ **HomeScreenModel**: 4 tests
- ✅ **AlbumsTabModel**: 4 tests
- ✅ **SearchTabModel**: 9 tests
- ✅ **PreferencesTabModel**: 2 tests

**Total**: 19 tests, 100% passing

See complete documentation at [docs/TESTING.md](docs/TESTING.md)

## 📄 License

This project is open source for educational purposes.

## 👨‍💻 Development

To contribute to the project:

1. Fork the repository
2. Create a branch for your feature (`git checkout -b feature/MyFeature`)
3. Commit your changes (`git commit -m 'Add MyFeature'`)
4. Push to the branch (`git push origin feature/MyFeature`)
5. Open a Pull Request
