package com.itunesexplorer.home.presentation.albums

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
class AlbumsTabModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeApi: FakeITunesApi

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeApi = FakeITunesApi()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load albums successfully on init`() = runTest(testDispatcher) {
        val viewModel = AlbumsTabModel(fakeApi)

        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.recommendations.size)
            assertNull(state.error)
        }
    }

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

    @Test
    fun `onAction Retry should reload recommendations`() = runTest(testDispatcher) {
        val viewModel = AlbumsTabModel(fakeApi)
        advanceUntilIdle()

        viewModel.state.test {
            // Skip current state
            awaitItem()

            // Retry
            viewModel.onAction(AlbumsIntent.Retry)

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            advanceUntilIdle()

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(2, successState.recommendations.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

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
}

class FakeITunesApi : ITunesApi {
    var shouldFail = false

    override suspend fun search(
        term: String,
        media: String?,
        entity: String?,
        attribute: String?,
        limit: Int,
        lang: String,
        country: String
    ): ITunesSearchResponse {
        throw NotImplementedError()
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
        if (shouldFail) {
            throw Exception("Network error")
        }
        return ITunesRssResponse(
            feed = RssFeed(
                entry = listOf(
                    createFeedEntry("Album 1"),
                    createFeedEntry("Album 2")
                )
            )
        )
    }

    private fun createFeedEntry(name: String) = RssFeedEntry(
        id = RssId(
            label = "1",
            attributes = RssId.RssIdAttributes(imId = "1")
        ),
        imName = RssLabel(name),
        imImage = listOf(
            RssImage(
                label = "https://example.com/image.jpg",
                attributes = RssImage.RssImageAttributes(height = "100")
            )
        ),
        title = RssLabel(name),
        link = RssLink(
            attributes = RssLink.RssLinkAttributes(href = "https://example.com")
        ),
        category = RssCategory(
            attributes = RssCategory.RssCategoryAttributes(label = "Rock")
        ),
        imArtist = RssArtist(label = "Artist"),
        imPrice = RssPrice(
            label = "$9.99",
            attributes = RssPrice.RssPriceAttributes(amount = "9.99", currency = "USD")
        ),
        imReleaseDate = RssReleaseDate(
            attributes = RssReleaseDate.RssReleaseDateAttributes(label = "2024-01-01")
        )
    )
}
