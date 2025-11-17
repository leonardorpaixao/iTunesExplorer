package com.itunesexplorer.catalog.presentation.albums

import com.itunesexplorer.catalog.data.api.CatalogApi
import com.itunesexplorer.catalog.data.models.ITunesRssResponse
import com.itunesexplorer.catalog.data.models.RssArtist
import com.itunesexplorer.catalog.data.models.RssCategory
import com.itunesexplorer.catalog.data.models.RssFeed
import com.itunesexplorer.catalog.data.models.RssFeedEntry
import com.itunesexplorer.catalog.data.models.RssId
import com.itunesexplorer.catalog.data.models.RssImage
import com.itunesexplorer.catalog.data.models.RssLabel
import com.itunesexplorer.catalog.data.models.RssLink
import com.itunesexplorer.catalog.data.models.RssPrice
import com.itunesexplorer.catalog.data.models.RssReleaseDate
import com.itunesexplorer.catalog.shared.data.models.*
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.ITunesItem
import com.itunesexplorer.network.models.ITunesSearchResponse
import com.itunesexplorer.network.models.MusicGenre
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

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsTabModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeCatalogApi: FakeCatalogApi
    private lateinit var fakeITunesApi: FakeITunesApi
    private lateinit var viewModel: AlbumsTabModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeCatalogApi = FakeCatalogApi()
        fakeITunesApi = FakeITunesApi()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have ALL genre selected and load top albums`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeCatalogApi, fakeITunesApi)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.ALL, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals(2, state.recommendations.size)
        assertEquals("Album 1", state.recommendations[0].imName.label)
    }

    @Test
    fun `onAction SelectGenre with ROCK should update selected genre and load rock albums`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeCatalogApi, fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ROCK))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.ROCK, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals(1, state.recommendations.size)
        assertEquals("Rock Album", state.recommendations[0].imName.label)
        assertEquals("Rock", state.recommendations[0].category.attributes.label)
    }

    @Test
    fun `onAction SelectGenre with JAZZ should update selected genre and load jazz albums`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeCatalogApi, fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.JAZZ))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.JAZZ, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals(1, state.recommendations.size)
        assertEquals("Jazz Album", state.recommendations[0].imName.label)
        assertEquals("Jazz", state.recommendations[0].category.attributes.label)
    }

    @Test
    fun `onAction SelectGenre with ALL should load top albums from RSS feed`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeCatalogApi, fakeITunesApi)
        advanceUntilIdle()

        // First select ROCK
        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ROCK))
        advanceUntilIdle()

        // Then select ALL
        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ALL))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.ALL, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals(2, state.recommendations.size)
        assertEquals("Album 1", state.recommendations[0].imName.label)
    }

    @Test
    fun `onAction SelectGenre with error should set error state`() = runTest(testDispatcher) {
        fakeITunesApi.shouldFail = true
        viewModel = AlbumsTabModel(fakeCatalogApi, fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ROCK))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.ROCK, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals("Failed to load albums", state.error)
    }

    @Test
    fun `onAction Retry with ALL genre should reload top albums`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeCatalogApi, fakeITunesApi)
        advanceUntilIdle()

        viewModel.onAction(AlbumsIntent.Retry)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.ALL, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals(2, state.recommendations.size)
    }

    @Test
    fun `onAction Retry with specific genre should reload that genre`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeCatalogApi, fakeITunesApi)
        advanceUntilIdle()

        // Select ROCK
        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ROCK))
        advanceUntilIdle()

        // Retry
        viewModel.onAction(AlbumsIntent.Retry)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.ROCK, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals(1, state.recommendations.size)
        assertEquals("Rock Album", state.recommendations[0].imName.label)
    }

    @Test
    fun `should switch between different genres correctly`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeCatalogApi, fakeITunesApi)
        advanceUntilIdle()

        // Select ROCK
        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ROCK))
        advanceUntilIdle()
        val rockState = viewModel.state.value
        assertEquals(MusicGenre.ROCK, rockState.selectedGenre)
        assertEquals("Rock Album", rockState.recommendations[0].imName.label)

        // Select JAZZ
        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.JAZZ))
        advanceUntilIdle()
        val jazzState = viewModel.state.value
        assertEquals(MusicGenre.JAZZ, jazzState.selectedGenre)
        assertEquals("Jazz Album", jazzState.recommendations[0].imName.label)

        // Back to ALL
        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ALL))
        advanceUntilIdle()
        val allState = viewModel.state.value
        assertEquals(MusicGenre.ALL, allState.selectedGenre)
        assertEquals(2, allState.recommendations.size)
    }
}

// Fake implementations for testing
class FakeCatalogApi : CatalogApi {
    var shouldFail = false

    override suspend fun topAlbums(limit: Int, country: String): ITunesRssResponse {
        if (shouldFail) throw Exception("Failed to load top albums")

        return ITunesRssResponse(
            feed = RssFeed(
                entry = listOf(
                    createRssFeedEntry("1", "Album 1", "Artist 1", "Pop"),
                    createRssFeedEntry("2", "Album 2", "Artist 2", "Rock")
                )
            )
        )
    }

    private fun createRssFeedEntry(
        id: String,
        name: String,
        artist: String,
        genre: String
    ): RssFeedEntry {
        return RssFeedEntry(
            id = RssId(
                label = id,
                attributes = RssId.RssIdAttributes(imId = id)
            ),
            imName = RssLabel(name),
            imImage = listOf(
                RssImage("https://example.com/image.jpg", RssImage.RssImageAttributes("100"))
            ),
            title = RssLabel(name),
            link = RssLink(
                attributes = RssLink.RssLinkAttributes(href = "https://example.com")
            ),
            category = RssCategory(
                attributes = RssCategory.RssCategoryAttributes(label = genre)
            ),
            imArtist = RssArtist(artist),
            imPrice = RssPrice(
                label = "$9.99",
                attributes = RssPrice.RssPriceAttributes(amount = "9.99", currency = "USD")
            ),
            imReleaseDate = RssReleaseDate(
                attributes = RssReleaseDate.RssReleaseDateAttributes(label = "2024-01-01")
            )
        )
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
        country: String?
    ): ITunesSearchResponse {
        throw NotImplementedError()
    }

    override suspend fun searchByGenre(
        genre: String,
        limit: Int,
        lang: String,
        country: String?
    ): ITunesSearchResponse {
        if (shouldFail) throw Exception("Failed to load albums")

        return when {
            genre.contains("rock", ignoreCase = true) -> {
                ITunesSearchResponse(
                    resultCount = 1,
                    results = listOf(
                        createITunesItem(1L, "Rock Album", "Rock Artist", "Rock")
                    )
                )
            }
            genre.contains("jazz", ignoreCase = true) -> {
                ITunesSearchResponse(
                    resultCount = 1,
                    results = listOf(
                        createITunesItem(2L, "Jazz Album", "Jazz Artist", "Jazz")
                    )
                )
            }
            else -> {
                ITunesSearchResponse(
                    resultCount = 0,
                    results = emptyList()
                )
            }
        }
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

    private fun createITunesItem(
        collectionId: Long,
        collectionName: String,
        artistName: String,
        genre: String
    ): ITunesItem {
        return ITunesItem(
            wrapperType = "collection",
            collectionId = collectionId,
            collectionName = collectionName,
            artistName = artistName,
            primaryGenreName = genre,
            artworkUrl60 = "https://example.com/art60.jpg",
            artworkUrl100 = "https://example.com/art100.jpg",
            collectionPrice = 9.99,
            currency = "USD",
            collectionViewUrl = "https://example.com/collection",
            releaseDate = "2024-01-01T00:00:00Z"
        )
    }
}
