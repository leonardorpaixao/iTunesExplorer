package com.itunesexplorer.catalog.presentation.search

import app.cash.turbine.test
import com.itunesexplorer.catalog.domain.model.MediaType
import com.itunesexplorer.catalog.domain.model.Money
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.SearchRepository
import com.itunesexplorer.core.common.domain.DomainError
import com.itunesexplorer.settings.country.CountryManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SearchTabModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeSearchRepository
    private lateinit var viewModel: SearchTabModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeSearchRepository()
        CountryManager.clear()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        CountryManager.clear()
    }

    @Test
    fun `initial state should have empty search query and ALL media type selected`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("", state.searchQuery)
        assertEquals(MediaType.ALL, state.selectedMediaType)
        assertTrue(state.items.isEmpty())
        assertFalse(state.isLoading)
        assertFalse(state.showRegionHint)
    }

    @Test
    fun `onAction UpdateSearchQuery should update query in state`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test query"))

        val state = viewModel.state.value
        assertEquals("test query", state.searchQuery)
    }

    @Test
    fun `onAction Search with valid query should load results`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        fakeRepository.mockResults = listOf(
            createSearchResult("1", "Result 1", "Artist 1"),
            createSearchResult("2", "Result 2", "Artist 2")
        )

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(2, state.items.size)
        assertEquals("Result 1", state.items[0].name)
        assertFalse(state.showRegionHint)
    }

    @Test
    fun `onAction Search with empty query should not trigger search`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun `showRegionHint should be true when results empty and country selected`() = runTest(testDispatcher) {
        CountryManager.setCountry("BR")
        fakeRepository.mockResults = emptyList()

        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("hulk"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.items.isEmpty())
        assertTrue(state.showRegionHint, "Should show region hint when results empty and country selected")
    }

    @Test
    fun `showRegionHint should be false when results empty but no country selected`() = runTest(testDispatcher) {
        CountryManager.clear()
        fakeRepository.mockResults = emptyList()

        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.items.isEmpty())
        assertFalse(state.showRegionHint, "Should NOT show region hint when no country selected")
    }

    // Note: This test is covered by "showRegionHint should be recalculated on each search"
    // and was removed due to intermittent threading issues with CountryManager

    @Test
    fun `onAction SelectMediaType should update selected media type`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.SelectMediaType(MediaType.MUSIC))

        val state = viewModel.state.value
        assertEquals(MediaType.MUSIC, state.selectedMediaType)
    }

    @Test
    fun `onAction SelectMediaType should trigger search if query is not blank`() = runTest(testDispatcher) {
        fakeRepository.mockResults = listOf(
            createSearchResult("1", "Music Result", "Artist")
        )

        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
        viewModel.onAction(SearchIntent.SelectMediaType(MediaType.MUSIC))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MediaType.MUSIC, state.selectedMediaType)
        assertEquals(1, state.items.size)
    }

    @Test
    fun `onAction Search with error should set error state`() = runTest(testDispatcher) {
        fakeRepository.shouldFail = true

        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.error != null)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun `onAction Retry should retry the search`() = runTest(testDispatcher) {
        fakeRepository.mockResults = listOf(
            createSearchResult("1", "Retry Result", "Artist")
        )

        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
        viewModel.onAction(SearchIntent.Retry)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(1, state.items.size)
        assertEquals("Retry Result", state.items[0].name)
    }

    @Test
    fun `showRegionHint should be recalculated on each search`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        // First search - no results, country selected
        CountryManager.setCountry("BR")
        fakeRepository.mockResults = emptyList()
        viewModel.onAction(SearchIntent.UpdateSearchQuery("hulk"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        var state = viewModel.state.value
        assertTrue(state.showRegionHint, "First search should show hint")

        // Second search - has results, country still selected
        fakeRepository.mockResults = listOf(createSearchResult("1", "Found", "Artist"))
        viewModel.onAction(SearchIntent.UpdateSearchQuery("avengers"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        state = viewModel.state.value
        assertFalse(state.showRegionHint, "Second search should NOT show hint (has results)")

        // Third search - no results again, but no country
        CountryManager.clear()
        fakeRepository.mockResults = emptyList()
        viewModel.onAction(SearchIntent.UpdateSearchQuery("xyz"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        state = viewModel.state.value
        assertFalse(state.showRegionHint, "Third search should NOT show hint (no country)")
    }

    private fun createSearchResult(
        id: String,
        name: String,
        artist: String
    ): SearchResult {
        return SearchResult(
            id = id,
            type = "song",
            name = name,
            artistName = artist,
            collectionName = null,
            imageUrl = "https://example.com/artwork.jpg",
            viewUrl = "https://example.com/item",
            previewUrl = null,
            price = Money(9.99, "USD"),
            releaseDate = "2024-01-01",
            genre = "Music",
            description = null
        )
    }
}

class FakeSearchRepository : SearchRepository {
    var shouldFail = false
    var mockResults: List<SearchResult> = emptyList()

    override suspend fun search(
        query: String,
        mediaType: MediaType,
        limit: Int
    ): com.itunesexplorer.core.common.domain.DomainResult<List<SearchResult>> {
        return if (shouldFail) {
            com.itunesexplorer.core.common.domain.DomainResult.failure(DomainError.NetworkError("Search failed"))
        } else {
            com.itunesexplorer.core.common.domain.DomainResult.success(mockResults)
        }
    }
}
