# MVI Architecture (Model-View-Intent)

This project uses the MVI (Model-View-Intent) architectural pattern for state management and unidirectional data flow.

## Overview

MVI is an architectural pattern that promotes:
- **Unidirectional data flow**: View → Intent → Model → View
- **Immutable state**: All states are represented by immutable data classes
- **Predictability**: Each Intent produces a deterministic result
- **Testability**: Easy to test Intents, States, and Effects in isolation
- **Traceability**: Simple to debug data flow

## Components

### 1. ViewState

Represents the complete UI state at a given moment.

```kotlin
data class HomeViewState(
    val selectedTab: HomeTab = HomeTab.ALBUMS
) : ViewState
```

**Characteristics:**
- Always an immutable `data class`
- Implements the `ViewState` interface
- Contains ALL data needed to render the UI
- Default values for initial state

### 2. ViewIntent

Represents user intentions/actions.

```kotlin
sealed class HomeIntent : ViewIntent {
    data class SelectTab(val tab: HomeTab) : HomeIntent()
}
```

**Characteristics:**
- Always a `sealed class` or `sealed interface`
- Implements the `ViewIntent` interface
- Each user action is a specific Intent
- Can contain data needed to process the action

### 3. ViewEffect

Represents side effects that are not part of the state.

```kotlin
sealed class SearchEffect : ViewEffect {
    data class ShowError(val message: String) : SearchEffect()
}
```

**Characteristics:**
- Always a `sealed class`
- Implements the `ViewEffect` interface
- Used for one-time events (toasts, navigation, etc.)
- Does not persist in state

### 4. MviViewModel

Base class that implements the MVI pattern.

```kotlin
abstract class MviViewModel<State : ViewState, Intent : ViewIntent, Effect : ViewEffect>(
    initialState: State
) : StateScreenModel<State>(initialState) {

    private val effectChannel = Channel<Effect>(Channel.BUFFERED)
    val effect: Flow<Effect> = effectChannel.receiveAsFlow()

    abstract fun onAction(intent: Intent)

    protected fun sendEffect(effect: Effect) {
        screenModelScope.launch {
            effectChannel.send(effect)
        }
    }
}
```

## Data Flow

```
┌─────────────────────────────────────────────────────────┐
│                         VIEW                            │
│  - Renders UI based on State                           │
│  - Sends Intents in response to user actions           │
│  - Observes Effects for one-time events                │
└────────────┬──────────────────────────┬─────────────────┘
             │                          │
             │ Intent                   │ State/Effect
             ▼                          │
┌─────────────────────────────────────┐ │
│         MviViewModel                │ │
│  - Receives Intent via onAction()   │ │
│  - Processes business logic         │ │
│  - Updates State (immutable)        │◄┘
│  - Sends Effects when needed        │
└─────────────────────────────────────┘
```

## Complete Example: SearchTabModel

### 1. Define State, Intent, and Effect

```kotlin
// State - What the UI needs to know
data class SearchViewState(
    val isLoading: Boolean = false,
    val items: List<ITunesItem> = emptyList(),
    val error: String? = null,
    val selectedMediaType: MediaType = MediaType.ALL,
    val searchQuery: String = ""
) : ViewState

// Intent - What the user can do
sealed class SearchIntent : ViewIntent {
    data class UpdateSearchQuery(val query: String) : SearchIntent()
    data object Search : SearchIntent()
    data class SelectMediaType(val mediaType: MediaType) : SearchIntent()
    data object Retry : SearchIntent()
}

// Effect - One-time events
sealed class SearchEffect : ViewEffect {
    data class ShowError(val message: String) : SearchEffect()
}
```

### 2. Implement ViewModel

```kotlin
class SearchTabModel(
    private val iTunesApi: ITunesApi
) : MviViewModel<SearchViewState, SearchIntent, SearchEffect>(
    initialState = SearchViewState()
) {

    init {
        onAction(SearchIntent.LoadTopContent)
    }

    override fun onAction(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is SearchIntent.Search -> search()
            is SearchIntent.SelectMediaType -> selectMediaType(intent.mediaType)
            is SearchIntent.Retry -> retry()
        }
    }

    private fun updateSearchQuery(query: String) {
        mutableState.update { it.copy(searchQuery = query) }
    }

    private fun search() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = iTunesApi.search(...)
                mutableState.update {
                    it.copy(isLoading = false, items = response.results)
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An error occurred"
                mutableState.update {
                    it.copy(isLoading = false, error = errorMessage)
                }
                sendEffect(SearchEffect.ShowError(errorMessage))
            }
        }
    }
}
```

### 3. Use in View

```kotlin
@Composable
fun SearchTab() {
    val screenModel: SearchTabModel by rememberInstance()
    val state by screenModel.state.collectAsState()

    // Observe effects
    LaunchedEffect(Unit) {
        screenModel.effect.collect { effect ->
            when (effect) {
                is SearchEffect.ShowError -> {
                    // Show toast, snackbar, etc.
                }
            }
        }
    }

    SearchTabContent(
        state = state,
        onAction = screenModel::onAction
    )
}

@Composable
fun SearchTabContent(
    state: SearchViewState,
    onAction: (SearchIntent) -> Unit
) {
    Column {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { onAction(SearchIntent.UpdateSearchQuery(it)) },
            onSearch = { onAction(SearchIntent.Search) }
        )

        when {
            state.isLoading -> LoadingIndicator()
            state.error != null -> ErrorMessage(
                message = state.error,
                onRetry = { onAction(SearchIntent.Retry) }
            )
            else -> ItemsList(state.items)
        }
    }
}
```

## Benefits

### 1. Testability

Tests are simple and straightforward:

```kotlin
@Test
fun `onAction UpdateSearchQuery should update query`() = runTest {
    val viewModel = SearchTabModel(fakeApi)

    viewModel.state.test {
        awaitItem() // initial state

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))

        val state = awaitItem()
        assertEquals("test", state.searchQuery)
    }
}
```

### 2. Debugging

Easy to trace the flow:
- Intent sent → logs
- State changed → logs
- Effect emitted → logs

### 3. Predictability

Same Intent + Same State = Same Result

### 4. Separation of Concerns

- **View**: Only rendering
- **ViewModel**: Only business logic
- **State**: Only data

## Error Handling with MVI

### Using DomainError

The project uses `DomainError` from `core:error` module for type-safe error handling:

```kotlin
// Domain error types
sealed class DomainError {
    data class NetworkError(val message: String) : DomainError()
    data class ApiError(val code: Int, val message: String) : DomainError()
    data class UnknownError(val throwable: Throwable) : DomainError()
}

// Extension function for safe error handling
suspend fun <T> runCatchingDomain(block: suspend () -> T): Either<DomainError, T>
```

### Integration with MVI

```kotlin
class MyScreenModel(
    private val repository: MyRepository
) : MviViewModel<MyViewState, MyIntent, MyEffect>(
    initialState = MyViewState()
) {

    private fun loadData() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true) }

            when (val result = repository.fetchData()) {
                is Either.Left -> {
                    val errorMessage = when (val error = result.value) {
                        is DomainError.NetworkError -> error.message
                        is DomainError.ApiError -> "API Error: ${error.message}"
                        is DomainError.UnknownError -> "Unknown error occurred"
                    }
                    mutableState.update {
                        it.copy(isLoading = false, error = errorMessage)
                    }
                    sendEffect(MyEffect.ShowError(errorMessage))
                }
                is Either.Right -> {
                    mutableState.update {
                        it.copy(isLoading = false, data = result.value)
                    }
                }
            }
        }
    }
}
```

## Settings and Preferences with MVI

### Settings Repository Pattern

The `core:settings` module provides user preferences management:

```kotlin
// Settings repository interface
interface SettingsRepository {
    suspend fun getCountry(): String
    suspend fun setCountry(country: String)
    suspend fun getLanguage(): String
    suspend fun setLanguage(language: String)
}
```

### Integration with MVI

```kotlin
class PreferencesTabModel(
    private val settingsRepository: SettingsRepository
) : MviViewModel<PreferencesViewState, PreferencesIntent, PreferencesEffect>(
    initialState = PreferencesViewState()
) {

    init {
        loadSettings()
    }

    override fun onAction(intent: PreferencesIntent) {
        when (intent) {
            is PreferencesIntent.SelectCountry -> updateCountry(intent.country)
            is PreferencesIntent.SelectLanguage -> updateLanguage(intent.language)
        }
    }

    private fun loadSettings() {
        screenModelScope.launch {
            val country = settingsRepository.getCountry()
            val language = settingsRepository.getLanguage()
            mutableState.update {
                it.copy(selectedCountry = country, selectedLanguage = language)
            }
        }
    }

    private fun updateCountry(country: String) {
        screenModelScope.launch {
            settingsRepository.setCountry(country)
            mutableState.update { it.copy(selectedCountry = country) }
        }
    }
}
```

**Key Points:**
- Settings are loaded in `init` block
- Settings updates are persisted immediately
- State reflects current settings for UI
- No effects needed for simple updates

## Best Practices

### ✅ DO

```kotlin
// Immutable state
data class MyState(val count: Int = 0) : ViewState

// Specific intent
sealed class MyIntent : ViewIntent {
    data class Increment(val amount: Int) : MyIntent()
}

// Immutable update
mutableState.update { it.copy(count = it.count + amount) }
```

### ❌ DON'T

```kotlin
// Mutable state
var count = 0

// Generic intent
data class UpdateState(val newState: MyState) : MyIntent()

// Direct update
state.count = state.count + 1
```

## ViewModels in the Project

### HomeScreenModel
- **State**: Selected tab
- **Intents**: SelectTab
- **Effects**: None (navigation is part of state)

### AlbumsTabModel
- **State**: Recommendations, loading, error, selected genre
- **Intents**: LoadRecommendations, SelectGenre, Retry
- **Effects**: ShowError

### SearchTabModel
- **State**: Items, query, mediaType, loading, error
- **Intents**: UpdateSearchQuery, Search, SelectMediaType, Retry
- **Effects**: ShowError

### PreferencesTabModel
- **State**: Selected country, selected language
- **Intents**: SelectCountry, SelectLanguage
- **Effects**: None (settings are persisted directly)

### DetailsScreenModel
- **State**: Item details, related items, loading, error
- **Intents**: Retry
- **Effects**: ShowError

## References

- [MVI Pattern](https://hannesdorfmann.com/android/mosby3-mvi-1/)
- [Unidirectional Data Flow](https://developer.android.com/topic/architecture/ui-layer#udf)
- [Voyager ScreenModel](https://voyager.adriel.cafe/screenmodel)
