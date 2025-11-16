# Arquitetura MVI (Model-View-Intent)

Este projeto utiliza o padrão arquitetural MVI (Model-View-Intent) para gerenciamento de estado e fluxo de dados unidirecional.

## Visão Geral

MVI é um padrão arquitetural que promove:
- **Fluxo unidirecional de dados**: View → Intent → Model → View
- **Estado imutável**: Todos os estados são representados por data classes imutáveis
- **Previsibilidade**: Cada Intent produz um resultado determinístico
- **Testabilidade**: Fácil testar Intents, States e Effects isoladamente
- **Rastreabilidade**: Simples debugar o fluxo de dados

## Componentes

### 1. ViewState

Representa o estado completo da UI em um determinado momento.

```kotlin
data class HomeViewState(
    val selectedTab: HomeTab = HomeTab.ALBUMS
) : ViewState
```

**Características:**
- Sempre uma `data class` imutável
- Implementa a interface `ViewState`
- Contém TODOS os dados necessários para renderizar a UI
- Valores padrão para estado inicial

### 2. ViewIntent

Representa as intenções/ações do usuário.

```kotlin
sealed class HomeIntent : ViewIntent {
    data class SelectTab(val tab: HomeTab) : HomeIntent()
}
```

**Características:**
- Sempre uma `sealed class` ou `sealed interface`
- Implementa a interface `ViewIntent`
- Cada ação do usuário é um Intent específico
- Pode conter dados necessários para processar a ação

### 3. ViewEffect

Representa efeitos colaterais (side effects) que não fazem parte do estado.

```kotlin
sealed class SearchEffect : ViewEffect {
    data class ShowError(val message: String) : SearchEffect()
}
```

**Características:**
- Sempre uma `sealed class`
- Implementa a interface `ViewEffect`
- Usado para eventos únicos (toasts, navegação, etc.)
- Não persiste no estado

### 4. MviViewModel

Base class que implementa o padrão MVI.

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

## Fluxo de Dados

```
┌─────────────────────────────────────────────────────────┐
│                         VIEW                            │
│  - Renderiza UI baseada no State                       │
│  - Envia Intents em resposta a ações do usuário        │
│  - Observa Effects para eventos únicos                 │
└────────────┬──────────────────────────┬─────────────────┘
             │                          │
             │ Intent                   │ State/Effect
             ▼                          │
┌─────────────────────────────────────┐ │
│         MviViewModel                │ │
│  - Recebe Intent via onAction()     │ │
│  - Processa lógica de negócio       │ │
│  - Atualiza State (imutável)        │◄┘
│  - Envia Effects quando necessário  │
└─────────────────────────────────────┘
```

## Exemplo Completo: SearchTabModel

### 1. Definir State, Intent e Effect

```kotlin
// State - O que a UI precisa saber
data class SearchViewState(
    val isLoading: Boolean = false,
    val items: List<ITunesItem> = emptyList(),
    val error: String? = null,
    val selectedMediaType: MediaType = MediaType.ALL,
    val searchQuery: String = ""
) : ViewState

// Intent - O que o usuário pode fazer
sealed class SearchIntent : ViewIntent {
    data class UpdateSearchQuery(val query: String) : SearchIntent()
    data object Search : SearchIntent()
    data class SelectMediaType(val mediaType: MediaType) : SearchIntent()
    data object Retry : SearchIntent()
}

// Effect - Eventos únicos
sealed class SearchEffect : ViewEffect {
    data class ShowError(val message: String) : SearchEffect()
}
```

### 2. Implementar ViewModel

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

### 3. Usar na View

```kotlin
@Composable
fun SearchTab() {
    val screenModel: SearchTabModel by rememberInstance()
    val state by screenModel.state.collectAsState()

    // Observar effects
    LaunchedEffect(Unit) {
        screenModel.effect.collect { effect ->
            when (effect) {
                is SearchEffect.ShowError -> {
                    // Mostrar toast, snackbar, etc.
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

## Benefícios

### 1. Testabilidade

Testes são simples e diretos:

```kotlin
@Test
fun `onAction UpdateSearchQuery should update query`() = runTest {
    val viewModel = SearchTabModel(fakeApi)

    viewModel.state.test {
        awaitItem() // estado inicial

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))

        val state = awaitItem()
        assertEquals("test", state.searchQuery)
    }
}
```

### 2. Debugging

Fácil rastrear o fluxo:
- Intent enviado → logs
- State alterado → logs
- Effect emitido → logs

### 3. Previsibilidade

Mesmo Intent + Mesmo State = Mesmo Resultado

### 4. Separação de Responsabilidades

- **View**: Apenas renderização
- **ViewModel**: Apenas lógica de negócio
- **State**: Apenas dados

## Melhores Práticas

### ✅ DO

```kotlin
// State imutável
data class MyState(val count: Int = 0) : ViewState

// Intent específico
sealed class MyIntent : ViewIntent {
    data class Increment(val amount: Int) : MyIntent()
}

// Atualização imutável
mutableState.update { it.copy(count = it.count + amount) }
```

### ❌ DON'T

```kotlin
// State mutável
var count = 0

// Intent genérico
data class UpdateState(val newState: MyState) : MyIntent()

// Atualização direta
state.count = state.count + 1
```

## ViewModels no Projeto

### HomeScreenModel
- **State**: Tab selecionada
- **Intents**: SelectTab
- **Effects**: Nenhum (navegação é parte do state)

### AlbumsTabModel
- **State**: Recomendações, loading, erro
- **Intents**: LoadRecommendations, Retry
- **Effects**: ShowError

### SearchTabModel
- **State**: Items, query, mediaType, loading, erro
- **Intents**: UpdateSearchQuery, Search, SelectMediaType, Retry
- **Effects**: ShowError

### PreferencesTabModel
- **State**: Placeholder (para expansão futura)
- **Intents**: (para expansão futura)
- **Effects**: (para expansão futura)

## Referências

- [MVI Pattern](https://hannesdorfmann.com/android/mosby3-mvi-1/)
- [Unidirectional Data Flow](https://developer.android.com/topic/architecture/ui-layer#udf)
- [Voyager ScreenModel](https://voyager.adriel.cafe/screenmodel)
