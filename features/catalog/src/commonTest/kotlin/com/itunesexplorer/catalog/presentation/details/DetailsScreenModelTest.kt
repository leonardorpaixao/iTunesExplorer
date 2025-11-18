package com.itunesexplorer.catalog.presentation.details

import app.cash.turbine.test
import com.itunesexplorer.catalog.domain.model.ItemDetails
import com.itunesexplorer.catalog.domain.model.Money
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.core.common.domain.DomainError
import com.itunesexplorer.core.common.domain.DomainResult
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
        val mainItem = createSearchResult("123", "Test Album", "Test Artist")
        val relatedItems = listOf(
            createSearchResult("124", "Track 1", "Test Artist"),
            createSearchResult("125", "Track 2", "Test Artist")
        )
        fakeRepository.mockItemDetails = ItemDetails(
            mainItem = mainItem,
            relatedItems = relatedItems,
        )

        viewModel = DetailsScreenModel(fakeRepository, "123")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertNotNull(state.item)
        assertEquals("Test Album", state.item?.name)
        assertEquals(2, state.relatedItems.size)
        assertEquals("Track 1", state.relatedItems[0].name)
    }

    @Test
    fun `should show loading state while fetching details`() = runTest(testDispatcher) {
        fakeRepository.mockItemDetails = ItemDetails(
            mainItem = createSearchResult("123", "Album", "Artist"),
            relatedItems = emptyList(),
        )

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
        assertTrue(state.error!!.contains("Failed"))
        assertNull(state.item)
        assertTrue(state.relatedItems.isEmpty())
    }

    @Test
    fun `should emit ShowError effect when loading fails`() = runTest(testDispatcher) {
        fakeRepository.shouldFail = true

        viewModel = DetailsScreenModel(fakeRepository, "123")

        viewModel.effect.test {
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is DetailsEffect.ShowError)
            assertTrue((effect as DetailsEffect.ShowError).message.contains("Failed"))
        }
    }

    @Test
    fun `onAction Retry should reload details`() = runTest(testDispatcher) {
        fakeRepository.mockItemDetails = ItemDetails(
            mainItem = createSearchResult("123", "Original", "Artist"),
            relatedItems = emptyList(),
        )

        viewModel = DetailsScreenModel(fakeRepository, "123")
        advanceUntilIdle()

        // Change the mock data
        fakeRepository.mockItemDetails = ItemDetails(
            mainItem = createSearchResult("123", "Updated", "Artist"),
            relatedItems = listOf(createSearchResult("124", "New Track", "Artist")),
        )

        viewModel.onAction(DetailsIntent.Retry)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Updated", state.item?.name)
        assertEquals(1, state.relatedItems.size)
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
        fakeRepository.mockItemDetails = ItemDetails(
            mainItem = createSearchResult("123", "Album", "Artist"),
            relatedItems = emptyList(),
        )

        viewModel.onAction(DetailsIntent.Retry)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNull(state.error)
        assertNotNull(state.item)
    }

    @Test
    fun `onAction OpenInStore should emit OpenUrl effect with item viewUrl`() = runTest(testDispatcher) {
        val testUrl = "https://music.apple.com/album/123"
        fakeRepository.mockItemDetails = ItemDetails(
            mainItem = createSearchResult("123", "Album", "Artist", viewUrl = testUrl),
            relatedItems = emptyList(),
        )

        viewModel = DetailsScreenModel(fakeRepository, "123")
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(DetailsIntent.OpenInStore)

            val effect = awaitItem()
            assertTrue(effect is DetailsEffect.OpenUrl)
            assertEquals(testUrl, (effect as DetailsEffect.OpenUrl).url)
        }
    }

    @Test
    fun `onAction OpenInStore should do nothing when item is null`() = runTest(testDispatcher) {
        fakeRepository.shouldFail = true

        viewModel = DetailsScreenModel(fakeRepository, "123")

        viewModel.effect.test {
            advanceUntilIdle() // This will trigger ShowError effect from init

            // Consume the ShowError effect from initialization
            val initEffect = awaitItem()
            assertTrue(initEffect is DetailsEffect.ShowError)

            // State has no item (error state)
            assertNull(viewModel.state.value.item)

            // Now try to open in store - should not emit anything new
            viewModel.onAction(DetailsIntent.OpenInStore)

            // Should not emit any new effect
            expectNoEvents()
        }
    }

    @Test
    fun `should handle item with no related items`() = runTest(testDispatcher) {
        fakeRepository.mockItemDetails = ItemDetails(
            mainItem = createSearchResult("123", "Single Track", "Artist"),
            relatedItems = emptyList(),
        )

        viewModel = DetailsScreenModel(fakeRepository, "123")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.item)
        assertEquals("Single Track", state.item?.name)
        assertTrue(state.relatedItems.isEmpty())
    }

    @Test
    fun `should handle item with multiple related items`() = runTest(testDispatcher) {
        val relatedItems = (1..10).map { i ->
            createSearchResult("12$i", "Track $i", "Artist")
        }

        fakeRepository.mockItemDetails = ItemDetails(
            mainItem = createSearchResult("100", "Album", "Artist"),
            relatedItems = relatedItems,
        )

        viewModel = DetailsScreenModel(fakeRepository, "100")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(10, state.relatedItems.size)
        assertEquals("Track 1", state.relatedItems[0].name)
        assertEquals("Track 10", state.relatedItems[9].name)
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
    var mockItemDetails: ItemDetails? = null

    override suspend fun getItemDetails(itemId: String): DomainResult<ItemDetails> {
        return if (shouldFail) {
            DomainResult.failure(DomainError.NetworkError("Failed to load item details"))
        } else {
            mockItemDetails?.let { DomainResult.success(it) }
                ?: DomainResult.failure(DomainError.NotFoundError("Item not found"))
        }
    }
}
