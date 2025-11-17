# Testing Guide

This document describes the testing strategy, tools, and best practices used in the project.

## Overview

The project uses unit tests for ViewModels based on the MVI architecture, ensuring that business logic works correctly.

## Testing Tools

### 1. Kotlin Test
Standard Kotlin testing framework.

```kotlin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
```

### 2. Kotlinx Coroutines Test
For testing asynchronous code with coroutines.

```kotlin
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
```

**Key features:**
- `StandardTestDispatcher`: Controllable dispatcher for tests
- `runTest`: Test scope for coroutines
- `advanceUntilIdle()`: Advances all pending coroutines

### 3. Turbine
Library for testing Flows in a simple and expressive way.

```kotlin
import app.cash.turbine.test

flow.test {
    assertEquals(expected, awaitItem())
    cancelAndIgnoreRemainingEvents()
}
```

## Test Structure

### Standard Setup

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MyViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeApi: FakeApi
    private lateinit var viewModel: MyViewModel

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
    fun `test description`() = runTest(testDispatcher) {
        // Arrange
        viewModel = MyViewModel(fakeApi)

        // Act
        advanceUntilIdle()

        // Assert
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(expected, state.property)
        }
    }
}
```

## Testing MVI ViewModels

### 1. Test Initial State

```kotlin
@Test
fun `should load data successfully on init`() = runTest(testDispatcher) {
    val viewModel = AlbumsTabModel(fakeApi)

    advanceUntilIdle()

    viewModel.state.test {
        val state = awaitItem()
        assertFalse(state.isLoading)
        assertEquals(2, state.recommendations.size)
        assertNull(state.error)
    }
}
```

**Key points:**
- Create ViewModel inside `runTest` to ensure correct dispatcher
- Use `advanceUntilIdle()` to complete async initialization
- Verify final state after initialization

### 2. Test Intents

```kotlin
@Test
fun `onAction UpdateSearchQuery should update query`() = runTest(testDispatcher) {
    val viewModel = SearchTabModel(fakeApi)
    advanceUntilIdle()

    viewModel.state.test {
        awaitItem() // current state

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))

        val state = awaitItem()
        assertEquals("test", state.searchQuery)
    }
}
```

**Key points:**
- Wait for initialization before sending intents
- `awaitItem()` to skip intermediate states
- Verify only the relevant change

### 3. Test Async Operations

```kotlin
@Test
fun `onAction Search should search with query`() = runTest(testDispatcher) {
    val viewModel = SearchTabModel(fakeApi)
    advanceUntilIdle()

    viewModel.state.test {
        awaitItem() // initial state

        viewModel.onAction(SearchIntent.UpdateSearchQuery("Beatles"))
        awaitItem() // query updated

        viewModel.onAction(SearchIntent.Search)
        awaitItem() // loading

        advanceUntilIdle() // wait for search

        val state = awaitItem() // result
        assertFalse(state.isLoading)
        assertEquals(2, state.items.size)
        assertEquals("Beatles", fakeApi.lastSearchTerm)

        cancelAndIgnoreRemainingEvents()
    }
}
```

**Key points:**
- `awaitItem()` for each state emission
- `advanceUntilIdle()` to complete async operations
- `cancelAndIgnoreRemainingEvents()` at the end

### 4. Test Error Handling

```kotlin
@Test
fun `should handle error on init`() = runTest(testDispatcher) {
    fakeApi.shouldFail = true
    val viewModel = AlbumsTabModel(fakeApi)

    advanceUntilIdle()

    viewModel.state.test {
        val state = awaitItem()
        assertFalse(state.isLoading)
        assertEquals(emptyList(), state.recommendations)
        assertEquals("Network error", state.error)
    }
}
```

### 5. Test Effects

```kotlin
@Test
fun `effect should be sent on error`() = runTest(testDispatcher) {
    fakeApi.shouldFail = true
    val viewModel = AlbumsTabModel(fakeApi)

    viewModel.effect.test {
        advanceUntilIdle()

        val effect = awaitItem()
        assertTrue(effect is AlbumsEffect.ShowError)
        assertEquals("Network error", (effect as AlbumsEffect.ShowError).message)
    }
}
```

## Testing Error Handling with DomainError

### Testing Repository Error Handling

When testing repositories that use `DomainError`, verify that errors are correctly mapped:

```kotlin
@Test
fun `should return NetworkError on network failure`() = runTest {
    // Arrange
    fakeApi.throwNetworkException = true
    val repository = MyRepositoryImpl(fakeApi)

    // Act
    val result = repository.fetchData()

    // Assert
    assertTrue(result is Either.Left)
    val error = (result as Either.Left).value
    assertTrue(error is DomainError.NetworkError)
    assertEquals("Network unavailable", error.message)
}

@Test
fun `should return ApiError on API failure`() = runTest {
    // Arrange
    fakeApi.responseCode = 404
    val repository = MyRepositoryImpl(fakeApi)

    // Act
    val result = repository.fetchData()

    // Assert
    assertTrue(result is Either.Left)
    val error = (result as Either.Left).value
    assertTrue(error is DomainError.ApiError)
    assertEquals(404, (error as DomainError.ApiError).code)
}
```

### Testing ViewModels with DomainError

```kotlin
@Test
fun `should handle NetworkError from repository`() = runTest(testDispatcher) {
    fakeRepository.returnError = DomainError.NetworkError("No connection")
    val viewModel = MyScreenModel(fakeRepository)

    advanceUntilIdle()

    viewModel.state.test {
        val state = awaitItem()
        assertEquals("No connection", state.error)
    }

    viewModel.effect.test {
        val effect = awaitItem()
        assertTrue(effect is MyEffect.ShowError)
        assertEquals("No connection", (effect as MyEffect.ShowError).message)
    }
}
```

## Testing Platform-Specific Code

### Expect/Actual Testing

When testing code with `expect/actual` declarations, create tests for each platform:

```kotlin
// commonTest
class CurrencyFormatterTest {
    @Test
    fun `should format USD correctly`() {
        val formatter = CurrencyFormatter()
        val result = formatter.format(99.99, "USD")
        assertTrue(result.contains("99.99"))
        assertTrue(result.contains("$") || result.contains("USD"))
    }
}

// androidTest (if platform-specific behavior needs testing)
class AndroidCurrencyFormatterTest {
    @Test
    fun `should use Android NumberFormat`() {
        val formatter = CurrencyFormatter()
        val result = formatter.format(1000.50, "USD")
        // Test Android-specific formatting behavior
        assertEquals("$1,000.50", result)
    }
}
```

**Key points:**
- Write common tests in `commonTest` for shared behavior
- Write platform-specific tests when behavior differs
- Use fake implementations to avoid platform dependencies

### Mocking Platform Dependencies

```kotlin
// Create a fake for platform-specific functionality
class FakeSettingsRepository : SettingsRepository {
    private var country = "US"
    private var language = "en"

    override suspend fun getCountry(): String = country
    override suspend fun setCountry(country: String) {
        this.country = country
    }
    override suspend fun getLanguage(): String = language
    override suspend fun setLanguage(language: String) {
        this.language = language
    }
}

@Test
fun `should load settings from repository`() = runTest(testDispatcher) {
    val fakeSettings = FakeSettingsRepository()
    fakeSettings.setCountry("BR")
    fakeSettings.setLanguage("pt")

    val viewModel = PreferencesTabModel(fakeSettings)
    advanceUntilIdle()

    viewModel.state.test {
        val state = awaitItem()
        assertEquals("BR", state.selectedCountry)
        assertEquals("pt", state.selectedLanguage)
    }
}
```

## Fake Implementations

### Implement Fake API

```kotlin
class FakeSearchApi : ITunesApi {
    var shouldFail = false
    var lastSearchTerm: String? = null
    var lastMediaType: String? = null

    override suspend fun search(
        term: String,
        media: String?,
        entity: String?,
        attribute: String?,
        limit: Int,
        lang: String,
        country: String
    ): ITunesSearchResponse {
        lastSearchTerm = term
        lastMediaType = media

        if (shouldFail) {
            throw Exception("Search error")
        }

        return ITunesSearchResponse(
            resultCount = 2,
            results = listOf(
                createItem("Item 1"),
                createItem("Item 2")
            )
        )
    }

    private fun createItem(name: String) = ITunesItem(
        trackId = 1,
        trackName = name,
        artistName = "Artist",
        collectionName = "Collection",
        artworkUrl100 = "https://example.com/image.jpg",
        trackPrice = 9.99,
        collectionPrice = 19.99,
        primaryGenreName = "Rock",
        releaseDate = "2024-01-01"
    )
}
```

**Characteristics of a good Fake:**
- Implements the same interface
- Allows controlling success/failure with flags
- Captures parameters for verification
- Returns realistic but simple data

## Running Tests

### Android Unit Tests

```bash
# Run all tests
./gradlew :features:home:testDebugUnitTest

# Run with report
./gradlew :features:home:testDebugUnitTest --continue

# View HTML report
open features/home/build/reports/tests/testDebugUnitTest/index.html
```

### Specific Tests

```bash
# Run a specific class
./gradlew :features:home:testDebugUnitTest --tests "HomeScreenModelTest"

# Run a specific test
./gradlew :features:home:testDebugUnitTest --tests "HomeScreenModelTest.should select tab correctly"
```

## Test Coverage

### What to Test

✅ **SHOULD be tested:**
- Initial states
- State changes via Intents
- Async operations (loading, success, error)
- Business logic
- Effects/Side effects
- Retry/Refresh logic
- Error handling scenarios
- Platform-agnostic behavior

❌ **DOES NOT need to be tested:**
- UI Composables (use UI tests for that)
- Simple navigation
- String formatting
- Trivial getters/setters

### Complete Coverage Example

For `SearchTabModel`:

```kotlin
class SearchTabModelTest {
    // ✅ Initial state
    @Test fun `should load top content on init`()

    // ✅ Basic intents
    @Test fun `onAction UpdateSearchQuery should update query`()

    // ✅ Async operations
    @Test fun `onAction Search should search with query`()
    @Test fun `onAction Search with empty query should load top content`()

    // ✅ Complex logic
    @Test fun `onAction SelectMediaType should update media type and search`()
    @Test fun `onAction SelectMediaType with query should search with query and media type`()

    // ✅ Error handling
    @Test fun `should handle search error`()
    @Test fun `onAction Retry should retry search`()

    // ✅ Effects
    @Test fun `effect should be sent on error`()
}
```

## Best Practices

### 1. Test Naming

```kotlin
// ✅ GOOD - Describes behavior
@Test
fun `should load albums successfully on init`()

@Test
fun `onAction SelectTab should update selected tab to SEARCH`()

// ❌ BAD - Does not describe behavior
@Test
fun testInit()

@Test
fun testSelectTab()
```

### 2. Arrange-Act-Assert

```kotlin
@Test
fun `should handle error correctly`() = runTest(testDispatcher) {
    // Arrange
    fakeApi.shouldFail = true
    val viewModel = AlbumsTabModel(fakeApi)

    // Act
    advanceUntilIdle()

    // Assert
    viewModel.state.test {
        val state = awaitItem()
        assertEquals("Network error", state.error)
    }
}
```

### 3. One Concept per Test

```kotlin
// ✅ GOOD - Tests only query update
@Test
fun `onAction UpdateSearchQuery should update query`() {
    viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
    assertEquals("test", state.searchQuery)
}

// ❌ BAD - Tests multiple concepts
@Test
fun `should update query and search and show results`() {
    // Too much code testing different things
}
```

### 4. Use cancelAndIgnoreRemainingEvents()

```kotlin
viewModel.state.test {
    // ... assertions

    // Always cancel at the end of tests with multiple emissions
    cancelAndIgnoreRemainingEvents()
}
```

### 5. Test Captured Values in Fake

```kotlin
@Test
fun `should pass correct parameters to API`() = runTest(testDispatcher) {
    val viewModel = SearchTabModel(fakeApi)
    advanceUntilIdle()

    viewModel.onAction(SearchIntent.SelectMediaType(MediaType.MUSIC))
    advanceUntilIdle()

    // Verify that the fake received the correct parameters
    assertEquals("music", fakeApi.lastMediaType)
}
```

## Troubleshooting

### Problem: Tests Timeout

```kotlin
// ❌ WRONG - Forgot advanceUntilIdle()
@Test
fun myTest() = runTest {
    viewModel.doAsync()
    // timeout waiting for coroutine to complete
}

// ✅ CORRECT
@Test
fun myTest() = runTest(testDispatcher) {
    viewModel.doAsync()
    advanceUntilIdle() // Advance coroutines
}
```

### Problem: Dispatcher Not Configured

```kotlin
// ❌ WRONG - ViewModel created before runTest
val viewModel = MyViewModel() // Uses wrong dispatcher

@Test
fun myTest() = runTest(testDispatcher) {
    // ...
}

// ✅ CORRECT
@Test
fun myTest() = runTest(testDispatcher) {
    val viewModel = MyViewModel() // Created inside runTest
    // ...
}
```

### Problem: State Does Not Emit

```kotlin
// ❌ WRONG - Does not wait for initialization
@Test
fun myTest() = runTest(testDispatcher) {
    val viewModel = MyViewModel()
    viewModel.state.test {
        // State has not been emitted yet
    }
}

// ✅ CORRECT
@Test
fun myTest() = runTest(testDispatcher) {
    val viewModel = MyViewModel()
    advanceUntilIdle() // Wait for initialization

    viewModel.state.test {
        val state = awaitItem()
        // ...
    }
}
```

## Test Results

### Current Status

```
✅ HomeScreenModelTest: 4 tests passing
✅ AlbumsTabModelTest: 4 tests passing
✅ SearchTabModelTest: 9 tests passing
✅ PreferencesTabModelTest: 2 tests passing

Total: 19 tests, 100% passing
```

## Additional Resources

- [Kotlin Test Documentation](https://kotlinlang.org/api/latest/kotlin.test/)
- [Coroutines Test Guide](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)
- [Turbine Documentation](https://github.com/cashapp/turbine)
- [Testing ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel#testing)
