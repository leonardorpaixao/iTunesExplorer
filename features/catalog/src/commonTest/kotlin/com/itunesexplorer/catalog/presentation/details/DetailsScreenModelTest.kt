package com.itunesexplorer.catalog.presentation.details

import app.cash.turbine.test
import com.itunesexplorer.catalog.domain.model.Money
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.core.error.DomainError
import com.itunesexplorer.core.error.DomainResult
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsScreenModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeDetailsRepository
    private lateinit var viewModel: DetailsScreenModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeDetailsRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should load details automatically`() = runTest(testDispatcher) {
        val item = createSearchResult("123", "Test Album", "Test Artist")
        fakeRepository.mockItem = item

        viewModel = DetailsScreenModel(fakeRepository, "123")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.item)
        assertEquals("Test Album", state.item?.name)
        assertEquals("Test Artist", state.item?.artistName)
    }

    @Test
    fun `should show loading state while fetching details`() = runTest(testDispatcher) {
        fakeRepository.mockItem = createSearchResult("123", "Album", "Artist")

        viewModel = DetailsScreenModel(fakeRepository, "123")

        viewModel.state.test {
            // Initial state already has isLoading = true (from initialState parameter)
            val initialState = awaitItem()
            assertTrue(initialState.isLoading, "Initial state should be loading")

            advanceUntilIdle()

            // After data loads
            val loadedState = awaitItem()
            assertFalse(loadedState.isLoading, "Should not be loading after data loaded")
            assertNotNull(loadedState.item)
        }
    }

    @Test
    fun `should set error state when loading fails`() = runTest(testDispatcher) {
        fakeRepository.shouldFail = true

        viewModel = DetailsScreenModel(fakeRepository, "123")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.error is DomainError.NetworkError)
        assertNull(state.item)
    }


    @Test
    fun `onAction Retry should reload details`() = runTest(testDispatcher) {
        fakeRepository.mockItem = createSearchResult("123", "Original", "Artist")

        viewModel = DetailsScreenModel(fakeRepository, "123")
        advanceUntilIdle()

        // Change the mock data
        fakeRepository.mockItem = createSearchResult("123", "Updated", "Artist")

        viewModel.onAction(DetailsIntent.Retry)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Updated", state.item?.name)
    }

    @Test
    fun `onAction Retry should clear previous error`() = runTest(testDispatcher) {
        fakeRepository.shouldFail = true

        viewModel = DetailsScreenModel(fakeRepository, "123")
        advanceUntilIdle()

        // Verify error state
        assertTrue(viewModel.state.value.error != null)

        // Fix the repository and retry
        fakeRepository.shouldFail = false
        fakeRepository.mockItem = createSearchResult("123", "Album", "Artist")

        viewModel.onAction(DetailsIntent.Retry)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNull(state.error)
        assertNotNull(state.item)
    }

    @Test
    fun `should load item details successfully`() = runTest(testDispatcher) {
        fakeRepository.mockItem = createSearchResult("123", "Single Track", "Artist")

        viewModel = DetailsScreenModel(fakeRepository, "123")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.item)
        assertEquals("Single Track", state.item?.name)
        assertEquals("Artist", state.item?.artistName)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `should preserve item data across state updates`() = runTest(testDispatcher) {
        val item = createSearchResult("100", "Album", "Artist")
        fakeRepository.mockItem = item

        viewModel = DetailsScreenModel(fakeRepository, "100")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Album", state.item?.name)
        assertEquals("Artist", state.item?.artistName)
        assertEquals("100", state.item?.id)
    }

    private fun createSearchResult(
        id: String,
        name: String,
        artist: String,
        viewUrl: String = "https://example.com/item/$id"
    ): SearchResult {
        return SearchResult(
            id = id,
            type = "song",
            name = name,
            artistName = artist,
            collectionName = "Test Collection",
            imageUrl = "https://example.com/artwork.jpg",
            viewUrl = viewUrl,
            previewUrl = "https://example.com/preview.m4a",
            price = Money(9.99, "USD"),
            releaseDate = "2024-01-01",
            genre = "Music",
            description = "Test description"
        )
    }
}

class FakeDetailsRepository : DetailsRepository {
    var shouldFail = false
    var mockItem: SearchResult? = null

    override suspend fun getItemDetails(itemId: String): DomainResult<SearchResult> {
        return if (shouldFail) {
            DomainResult.failure(DomainError.NetworkError("Failed to load item details"))
        } else {
            mockItem?.let { DomainResult.success(it) }
                ?: DomainResult.failure(DomainError.NotFoundError("Item not found"))
        }
    }
}
