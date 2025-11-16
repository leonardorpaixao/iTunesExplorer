# Guia de Testes

Este documento descreve a estratégia de testes, ferramentas e melhores práticas usadas no projeto.

## Visão Geral

O projeto utiliza testes unitários para ViewModels baseados na arquitetura MVI, garantindo que a lógica de negócio funcione corretamente.

## Ferramentas de Teste

### 1. Kotlin Test
Framework de testes padrão do Kotlin.

```kotlin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
```

### 2. Kotlinx Coroutines Test
Para testar código assíncrono com coroutines.

```kotlin
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
```

**Principais funcionalidades:**
- `StandardTestDispatcher`: Dispatcher controlável para testes
- `runTest`: Escopo de teste para coroutines
- `advanceUntilIdle()`: Avança todas as coroutines pendentes

### 3. Turbine
Biblioteca para testar Flows de forma simples e expressiva.

```kotlin
import app.cash.turbine.test

flow.test {
    assertEquals(expected, awaitItem())
    cancelAndIgnoreRemainingEvents()
}
```

## Estrutura de Testes

### Setup Padrão

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

## Testando ViewModels MVI

### 1. Testar Estado Inicial

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

**Pontos importantes:**
- Criar ViewModel dentro de `runTest` para garantir dispatcher correto
- Usar `advanceUntilIdle()` para completar inicialização assíncrona
- Verificar estado final após inicialização

### 2. Testar Intents

```kotlin
@Test
fun `onAction UpdateSearchQuery should update query`() = runTest(testDispatcher) {
    val viewModel = SearchTabModel(fakeApi)
    advanceUntilIdle()

    viewModel.state.test {
        awaitItem() // estado atual

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))

        val state = awaitItem()
        assertEquals("test", state.searchQuery)
    }
}
```

**Pontos importantes:**
- Aguardar inicialização antes de enviar intents
- `awaitItem()` para pular estados intermediários
- Verificar apenas a mudança relevante

### 3. Testar Operações Assíncronas

```kotlin
@Test
fun `onAction Search should search with query`() = runTest(testDispatcher) {
    val viewModel = SearchTabModel(fakeApi)
    advanceUntilIdle()

    viewModel.state.test {
        awaitItem() // estado inicial

        viewModel.onAction(SearchIntent.UpdateSearchQuery("Beatles"))
        awaitItem() // query atualizada

        viewModel.onAction(SearchIntent.Search)
        awaitItem() // loading

        advanceUntilIdle() // aguardar busca

        val state = awaitItem() // resultado
        assertFalse(state.isLoading)
        assertEquals(2, state.items.size)
        assertEquals("Beatles", fakeApi.lastSearchTerm)

        cancelAndIgnoreRemainingEvents()
    }
}
```

**Pontos importantes:**
- `awaitItem()` para cada emissão de estado
- `advanceUntilIdle()` para completar operações assíncronas
- `cancelAndIgnoreRemainingEvents()` no final

### 4. Testar Tratamento de Erros

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

### 5. Testar Effects

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

## Fake Implementations

### Implementar Fake API

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

**Características de um bom Fake:**
- Implementa a mesma interface
- Permite controlar sucesso/falha com flags
- Captura parâmetros para verificação
- Retorna dados realistas mas simples

## Executar Testes

### Android Unit Tests

```bash
# Executar todos os testes
./gradlew :features:home:testDebugUnitTest

# Executar com relatório
./gradlew :features:home:testDebugUnitTest --continue

# Ver relatório HTML
open features/home/build/reports/tests/testDebugUnitTest/index.html
```

### Testes Específicos

```bash
# Rodar uma classe específica
./gradlew :features:home:testDebugUnitTest --tests "HomeScreenModelTest"

# Rodar um teste específico
./gradlew :features:home:testDebugUnitTest --tests "HomeScreenModelTest.should select tab correctly"
```

## Cobertura de Testes

### O que Testar

✅ **DEVE ser testado:**
- Estados iniciais
- Mudanças de estado via Intents
- Operações assíncronas (loading, success, error)
- Lógica de negócio
- Effects/Side effects
- Retry/Refresh logic

❌ **NÃO precisa ser testado:**
- UI Composables (use testes de UI para isso)
- Navegação simples
- Formatação de strings
- Getters/Setters triviais

### Exemplo de Cobertura Completa

Para `SearchTabModel`:

```kotlin
class SearchTabModelTest {
    // ✅ Estado inicial
    @Test fun `should load top content on init`()

    // ✅ Intents básicos
    @Test fun `onAction UpdateSearchQuery should update query`()

    // ✅ Operações assíncronas
    @Test fun `onAction Search should search with query`()
    @Test fun `onAction Search with empty query should load top content`()

    // ✅ Lógica complexa
    @Test fun `onAction SelectMediaType should update media type and search`()
    @Test fun `onAction SelectMediaType with query should search with query and media type`()

    // ✅ Error handling
    @Test fun `should handle search error`()
    @Test fun `onAction Retry should retry search`()

    // ✅ Effects
    @Test fun `effect should be sent on error`()
}
```

## Melhores Práticas

### 1. Nomenclatura de Testes

```kotlin
// ✅ BOM - Descreve comportamento
@Test
fun `should load albums successfully on init`()

@Test
fun `onAction SelectTab should update selected tab to SEARCH`()

// ❌ RUIM - Não descreve comportamento
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

### 3. Um Conceito por Teste

```kotlin
// ✅ BOM - Testa apenas query update
@Test
fun `onAction UpdateSearchQuery should update query`() {
    viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
    assertEquals("test", state.searchQuery)
}

// ❌ RUIM - Testa múltiplos conceitos
@Test
fun `should update query and search and show results`() {
    // Muito código testando coisas diferentes
}
```

### 4. Usar cancelAndIgnoreRemainingEvents()

```kotlin
viewModel.state.test {
    // ... assertions

    // Sempre cancelar no final de testes com múltiplas emissões
    cancelAndIgnoreRemainingEvents()
}
```

### 5. Testar Valores Capturados no Fake

```kotlin
@Test
fun `should pass correct parameters to API`() = runTest(testDispatcher) {
    val viewModel = SearchTabModel(fakeApi)
    advanceUntilIdle()

    viewModel.onAction(SearchIntent.SelectMediaType(MediaType.MUSIC))
    advanceUntilIdle()

    // Verificar que o fake recebeu os parâmetros corretos
    assertEquals("music", fakeApi.lastMediaType)
}
```

## Troubleshooting

### Problema: Testes timeout

```kotlin
// ❌ ERRADO - Esqueceu advanceUntilIdle()
@Test
fun myTest() = runTest {
    viewModel.doAsync()
    // timeout esperando coroutine completar
}

// ✅ CORRETO
@Test
fun myTest() = runTest(testDispatcher) {
    viewModel.doAsync()
    advanceUntilIdle() // Avança coroutines
}
```

### Problema: Dispatcher não configurado

```kotlin
// ❌ ERRADO - ViewModel criado antes de runTest
val viewModel = MyViewModel() // Usa dispatcher errado

@Test
fun myTest() = runTest(testDispatcher) {
    // ...
}

// ✅ CORRETO
@Test
fun myTest() = runTest(testDispatcher) {
    val viewModel = MyViewModel() // Criado dentro de runTest
    // ...
}
```

### Problema: State não emite

```kotlin
// ❌ ERRADO - Não aguarda inicialização
@Test
fun myTest() = runTest(testDispatcher) {
    val viewModel = MyViewModel()
    viewModel.state.test {
        // Estado ainda não foi emitido
    }
}

// ✅ CORRETO
@Test
fun myTest() = runTest(testDispatcher) {
    val viewModel = MyViewModel()
    advanceUntilIdle() // Aguarda inicialização

    viewModel.state.test {
        val state = awaitItem()
        // ...
    }
}
```

## Resultado dos Testes

### Status Atual

```
✅ HomeScreenModelTest: 4 testes passando
✅ AlbumsTabModelTest: 4 testes passando
✅ SearchTabModelTest: 9 testes passando
✅ PreferencesTabModelTest: 2 testes passando

Total: 19 testes, 100% passando
```

## Recursos Adicionais

- [Kotlin Test Documentation](https://kotlinlang.org/api/latest/kotlin.test/)
- [Coroutines Test Guide](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)
- [Turbine Documentation](https://github.com/cashapp/turbine)
- [Testing ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel#testing)
