package com.itunesexplorer.catalog.presentation.search

import app.cash.turbine.test
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.ITunesItem
import com.itunesexplorer.network.models.ITunesSearchResponse
import com.itunesexplorer.network.models.MediaType
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
    private lateinit var fakeITunesApi: FakeITunesApi
    private lateinit var viewModel: SearchTabModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeITunesApi = FakeITunesApi()
        CountryManager.clear() // Clear country before each test
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        CountryManager.clear() // Clean up after each test
    }

    @Test
    fun `initial state should have empty search query and ALL media type selected`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeITunesApi)
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
        viewModel = SearchTabModel(fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test query"))

        val state = viewModel.state.value
        assertEquals("test query", state.searchQuery)
    }

    @Test
    fun `onAction Search with valid query should load results`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeITunesApi)
        advanceUntilIdle()

        fakeITunesApi.mockResults = listOf(
            createITunesItem(1L, "Result 1", "Artist 1"),
            createITunesItem(2L, "Result 2", "Artist 2")
        )

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(2, state.items.size)
        assertEquals("Result 1", state.items[0].trackName)
        assertFalse(state.showRegionHint)
    }

    @Test
    fun `onAction Search with empty query should not trigger search`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun `showRegionHint should be true when results empty and country selected`() = runTest(testDispatcher) {
        CountryManager.setCountry("BR") // Set country
        fakeITunesApi.mockResults = emptyList()

        viewModel = SearchTabModel(fakeITunesApi)
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
        CountryManager.clear() // No country selected
        fakeITunesApi.mockResults = emptyList()

        viewModel = SearchTabModel(fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.items.isEmpty())
        assertFalse(state.showRegionHint, "Should NOT show region hint when no country selected")
    }

    @Test
    fun `showRegionHint should be false when results are not empty`() = runTest(testDispatcher) {
        CountryManager.setCountry("BR")
        fakeITunesApi.mockResults = listOf(
            createITunesItem(1L, "Result 1", "Artist 1")
        )

        viewModel = SearchTabModel(fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.items.isEmpty())
        assertFalse(state.showRegionHint, "Should NOT show region hint when results are not empty")
    }

    @Test
    fun `onAction SelectMediaType should update selected media type`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.SelectMediaType(MediaType.MUSIC))

        val state = viewModel.state.value
        assertEquals(MediaType.MUSIC, state.selectedMediaType)
    }

    @Test
    fun `onAction SelectMediaType should trigger search if query is not blank`() = runTest(testDispatcher) {
        fakeITunesApi.mockResults = listOf(
            createITunesItem(1L, "Music Result", "Artist")
        )

        viewModel = SearchTabModel(fakeITunesApi)
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
        fakeITunesApi.shouldFail = true

        viewModel = SearchTabModel(fakeITunesApi)
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
        fakeITunesApi.mockResults = listOf(
            createITunesItem(1L, "Retry Result", "Artist")
        )

        viewModel = SearchTabModel(fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(SearchIntent.UpdateSearchQuery("test"))
        viewModel.onAction(SearchIntent.Retry)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(1, state.items.size)
        assertEquals("Retry Result", state.items[0].trackName)
    }

    @Test
    fun `showRegionHint should be recalculated on each search`() = runTest(testDispatcher) {
        viewModel = SearchTabModel(fakeITunesApi)
        advanceUntilIdle()

        // First search - no results, country selected
        CountryManager.setCountry("BR")
        fakeITunesApi.mockResults = emptyList()
        viewModel.onAction(SearchIntent.UpdateSearchQuery("hulk"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        var state = viewModel.state.value
        assertTrue(state.showRegionHint, "First search should show hint")

        // Second search - has results, country still selected
        fakeITunesApi.mockResults = listOf(createITunesItem(1L, "Found", "Artist"))
        viewModel.onAction(SearchIntent.UpdateSearchQuery("avengers"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        state = viewModel.state.value
        assertFalse(state.showRegionHint, "Second search should NOT show hint (has results)")

        // Third search - no results again, but no country
        CountryManager.clear()
        fakeITunesApi.mockResults = emptyList()
        viewModel.onAction(SearchIntent.UpdateSearchQuery("xyz"))
        viewModel.onAction(SearchIntent.Search)
        advanceUntilIdle()

        state = viewModel.state.value
        assertFalse(state.showRegionHint, "Third search should NOT show hint (no country)")
    }

    private fun createITunesItem(
        id: Long,
        name: String,
        artist: String,
        genre: String = "Music"
    ): ITunesItem {
        return ITunesItem(
            trackId = id,
            collectionId = id,
            trackName = name,
            collectionName = name,
            artistName = artist,
            artworkUrl100 = "https://example.com/artwork.jpg",
            trackPrice = 9.99,
            collectionPrice = 9.99,
            primaryGenreName = genre,
            releaseDate = "2024-01-01",
            trackCount = 10,
            kind = "song",
            shortDescription = "Short description",
            longDescription = "Long description"
        )
    }
}

class FakeITunesApi : ITunesApi {
    var shouldFail = false
    var mockResults: List<ITunesItem> = emptyList()

    override suspend fun search(
        term: String,
        media: String?,
        entity: String?,
        attribute: String?,
        limit: Int,
        lang: String,
        country: String?
    ): ITunesSearchResponse {
        if (shouldFail) throw Exception("Search failed")

        return ITunesSearchResponse(
            resultCount = mockResults.size,
            results = mockResults
        )
    }

    override suspend fun searchByGenre(
        genre: String,
        limit: Int,
        lang: String,
        country: String?
    ): ITunesSearchResponse {
        if (shouldFail) throw Exception("Search by genre failed")

        return ITunesSearchResponse(
            resultCount = mockResults.size,
            results = mockResults
        )
    }

    override suspend fun details(
        id: String?,
        amgArtistId: String?,
        upc: String?,
        isbn: String?,
        entity: String?,
        limit: Int,
        sort: String
    ): ITunesSearchResponse {
        if (shouldFail) throw Exception("Details failed")

        return ITunesSearchResponse(
            resultCount = mockResults.size,
            results = mockResults
        )
    }
}
