package com.itunesexplorer.home.presentation.search

import app.cash.turbine.test
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.*
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SearchTabModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeApi: FakeSearchApi

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeApi = FakeSearchApi()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load top content on init`() = runTest(testDispatcher) {
        val viewModel = SearchTabModel(fakeApi)

        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("", state.searchQuery)
            assertEquals(MediaType.ALL, state.selectedMediaType)
            assertEquals(2, state.items.size)
        }
    }

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

    @Test
    fun `onAction Search should search with query`() = runTest(testDispatcher) {
        val viewModel = SearchTabModel(fakeApi)
        advanceUntilIdle()

        viewModel.state.test {
            awaitItem()

            viewModel.onAction(SearchIntent.UpdateSearchQuery("Beatles"))
            awaitItem()

            viewModel.onAction(SearchIntent.Search)
            awaitItem() // loading

            advanceUntilIdle()

            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.items.size)
            assertEquals("Beatles", fakeApi.lastSearchTerm)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction Search with empty query should load top content`() = runTest(testDispatcher) {
        val viewModel = SearchTabModel(fakeApi)
        advanceUntilIdle()

        viewModel.state.test {
            awaitItem()

            viewModel.onAction(SearchIntent.Search)
            awaitItem() // loading

            advanceUntilIdle()
            awaitItem()

            assertEquals("top", fakeApi.lastSearchTerm)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction SelectMediaType should update media type and search`() = runTest(testDispatcher) {
        val viewModel = SearchTabModel(fakeApi)
        advanceUntilIdle()

        viewModel.state.test {
            awaitItem()

            viewModel.onAction(SearchIntent.SelectMediaType(MediaType.MUSIC))

            awaitItem() // media type updated
            awaitItem() // loading

            advanceUntilIdle()
            awaitItem()

            assertEquals("music", fakeApi.lastMediaType)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction SelectMediaType with query should search with query and media type`() = runTest(testDispatcher) {
        val viewModel = SearchTabModel(fakeApi)
        advanceUntilIdle()

        viewModel.state.test {
            awaitItem()

            viewModel.onAction(SearchIntent.UpdateSearchQuery("Beatles"))
            awaitItem()

            viewModel.onAction(SearchIntent.SelectMediaType(MediaType.MUSIC))
            awaitItem() // media type updated
            awaitItem() // loading

            advanceUntilIdle()
            awaitItem() // success

            assertEquals("Beatles", fakeApi.lastSearchTerm)
            assertEquals("music", fakeApi.lastMediaType)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAction Retry should retry search`() = runTest(testDispatcher) {
        fakeApi.shouldFail = true
        val viewModel = SearchTabModel(fakeApi)
        advanceUntilIdle()

        viewModel.state.test {
            val errorState = awaitItem()
            assertTrue(errorState.error != null)

            fakeApi.shouldFail = false
            viewModel.onAction(SearchIntent.Retry)

            awaitItem() // loading

            advanceUntilIdle()

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertNull(successState.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should handle search error`() = runTest(testDispatcher) {
        fakeApi.shouldFail = true
        val viewModel = SearchTabModel(fakeApi)

        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("Search error", state.error)
            assertEquals(emptyList(), state.items)
        }
    }

    @Test
    fun `effect should be sent on error`() = runTest(testDispatcher) {
        fakeApi.shouldFail = true
        val viewModel = SearchTabModel(fakeApi)

        viewModel.effect.test {
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is SearchEffect.ShowError)
            assertEquals("Search error", (effect as SearchEffect.ShowError).message)
        }
    }
}

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

    override suspend fun details(
        id: String?,
        amgArtistId: String?,
        upc: String?,
        isbn: String?,
        entity: String?,
        limit: Int,
        sort: String
    ): ITunesSearchResponse {
        throw NotImplementedError()
    }

    override suspend fun topAlbums(limit: Int, country: String): ITunesRssResponse {
        throw NotImplementedError()
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
