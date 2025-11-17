package com.itunesexplorer.catalog.presentation.albums

import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.catalog.domain.model.Money
import com.itunesexplorer.catalog.domain.repository.AlbumsRepository
import com.itunesexplorer.core.common.domain.DomainError
import com.itunesexplorer.core.common.domain.DomainResult
import com.itunesexplorer.network.models.MusicGenre
import com.itunesexplorer.settings.country.CountryManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsTabModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeAlbumsRepository
    private lateinit var viewModel: AlbumsTabModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeAlbumsRepository()
        CountryManager.clear()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        CountryManager.clear()
    }

    @Test
    fun `initial state should have ALL genre selected and load top albums`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.ALL, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals(2, state.recommendations.size)
        assertEquals("Album 1", state.recommendations[0].name)
        assertEquals("Artist 1", state.recommendations[0].artistName)
    }

    @Test
    fun `onAction SelectGenre with ROCK should update selected genre and load rock albums`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ROCK))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.ROCK, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals(1, state.recommendations.size)
        assertEquals("Rock Album", state.recommendations[0].name)
        assertEquals("Rock", state.recommendations[0].genre)
    }

    @Test
    fun `onAction SelectGenre with JAZZ should update selected genre and load jazz albums`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.JAZZ))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.JAZZ, state.selectedGenre)
        assertFalse(state.isLoading)
        assertEquals(1, state.recommendations.size)
        assertEquals("Jazz Album", state.recommendations[0].name)
        assertEquals("Jazz", state.recommendations[0].genre)
    }

    @Test
    fun `onAction SelectGenre with ALL should load top albums`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeRepository, CountryManager)
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
        assertEquals("Album 1", state.recommendations[0].name)
    }

    @Test
    fun `onAction SelectGenre with error should set error state`() = runTest(testDispatcher) {
        fakeRepository.shouldFailOnGenre = true
        viewModel = AlbumsTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ROCK))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MusicGenre.ROCK, state.selectedGenre)
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error!!.contains("Failed"))
    }

    @Test
    fun `onAction Retry with ALL genre should reload top albums`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeRepository, CountryManager)
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
        viewModel = AlbumsTabModel(fakeRepository, CountryManager)
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
        assertEquals("Rock Album", state.recommendations[0].name)
    }

    @Test
    fun `should switch between different genres correctly`() = runTest(testDispatcher) {
        viewModel = AlbumsTabModel(fakeRepository, CountryManager)
        advanceUntilIdle()

        // Select ROCK
        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ROCK))
        advanceUntilIdle()
        val rockState = viewModel.state.value
        assertEquals(MusicGenre.ROCK, rockState.selectedGenre)
        assertEquals("Rock Album", rockState.recommendations[0].name)

        // Select JAZZ
        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.JAZZ))
        advanceUntilIdle()
        val jazzState = viewModel.state.value
        assertEquals(MusicGenre.JAZZ, jazzState.selectedGenre)
        assertEquals("Jazz Album", jazzState.recommendations[0].name)

        // Back to ALL
        viewModel.onAction(AlbumsIntent.SelectGenre(MusicGenre.ALL))
        advanceUntilIdle()
        val allState = viewModel.state.value
        assertEquals(MusicGenre.ALL, allState.selectedGenre)
        assertEquals(2, allState.recommendations.size)
    }
}

// Fake implementations for testing
class FakeAlbumsRepository : AlbumsRepository {
    var shouldFailOnTop = false
    var shouldFailOnGenre = false

    override suspend fun getTopAlbums(limit: Int): DomainResult<List<Album>> {
        return if (shouldFailOnTop) {
            DomainResult.failure(DomainError.NetworkError("Failed to load top albums"))
        } else {
            DomainResult.success(
                listOf(
                    createAlbum("1", "Album 1", "Artist 1", "Pop"),
                    createAlbum("2", "Album 2", "Artist 2", "Rock")
                )
            )
        }
    }

    override suspend fun getAlbumsByGenre(genre: MusicGenre, limit: Int): DomainResult<List<Album>> {
        return if (shouldFailOnGenre) {
            DomainResult.failure(DomainError.NetworkError("Failed to load albums by genre"))
        } else {
            val album = when (genre) {
                MusicGenre.ROCK -> createAlbum("3", "Rock Album", "Rock Artist", "Rock")
                MusicGenre.JAZZ -> createAlbum("4", "Jazz Album", "Jazz Artist", "Jazz")
                else -> createAlbum("5", "Pop Album", "Pop Artist", "Pop")
            }
            DomainResult.success(listOf(album))
        }
    }

    private fun createAlbum(
        id: String,
        name: String,
        artist: String,
        genre: String
    ): Album {
        return Album(
            id = id,
            name = name,
            artistName = artist,
            imageUrl = "https://example.com/image.jpg",
            viewUrl = "https://example.com/album/$id",
            price = Money(9.99, "USD"),
            releaseDate = "2024-01-01",
            genre = genre
        )
    }
}

