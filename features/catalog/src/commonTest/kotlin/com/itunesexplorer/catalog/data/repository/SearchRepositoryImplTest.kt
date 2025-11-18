package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.data.api.ITunesItem
import com.itunesexplorer.catalog.data.api.ITunesSearchResponse
import com.itunesexplorer.catalog.domain.model.MediaType
import com.itunesexplorer.catalog.domain.repository.SearchRepository
import com.itunesexplorer.core.common.domain.DomainError
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.language.LanguageManager
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchRepositoryImplTest {

    private lateinit var fakeApi: FakeITunesApi
    private lateinit var repository: SearchRepository

    @BeforeTest
    fun setup() {
        fakeApi = FakeITunesApi()
        CountryManager.clear()
        repository = SearchRepositoryImpl(
            api = fakeApi,
            countryManager = CountryManager,
            languageManager = LanguageManager
        )
    }

    @AfterTest
    fun tearDown() {
        CountryManager.clear()
    }

    @Test
    fun `search should return domain models on success`() = runTest {
        // Given
        fakeApi.mockResults = listOf(
            createITunesItem(1L, "Song 1", "Artist 1"),
            createITunesItem(2L, "Song 2", "Artist 2")
        )

        // When
        val result = repository.search("test", MediaType.MUSIC, 50)

        // Then
        assertTrue(result.isSuccess())
        val items = result.getOrNull()!!
        assertEquals(2, items.size)
        assertEquals("Song 1", items[0].name)
        assertEquals("Artist 1", items[0].artistName)
        assertEquals("1", items[0].id)
    }

    @Test
    fun `search should return empty list when no results`() = runTest {
        // Given
        fakeApi.mockResults = emptyList()

        // When
        val result = repository.search("xyz", MediaType.ALL, 50)

        // Then
        assertTrue(result.isSuccess())
        val items = result.getOrNull()!!
        assertTrue(items.isEmpty())
    }

    @Test
    fun `search should return NetworkError when api throws exception`() = runTest {
        // Given
        fakeApi.shouldFail = true

        // When
        val result = repository.search("test", MediaType.MUSIC, 50)

        // Then
        assertTrue(result.isFailure())
        val error = result.errorOrNull()!!
        assertTrue(error is DomainError.UnknownError)
    }

    @Test
    fun `search should use country from manager`() = runTest {
        // Given
        CountryManager.setCountry("BR")
        fakeApi.mockResults = listOf(createITunesItem(1L, "Test", "Artist"))

        // When
        repository.search("test", MediaType.MUSIC, 50)

        // Then
        assertEquals("BR", fakeApi.lastCountry)
    }

    @Test
    fun `search should pass correct media type to API`() = runTest {
        // Given
        fakeApi.mockResults = emptyList()

        // When - MediaType.ALL should pass null to API
        repository.search("test", MediaType.ALL, 50)

        // Then
        assertEquals(null, fakeApi.lastMedia)

        // When - specific media type should pass value
        repository.search("test", MediaType.PODCAST, 50)

        // Then
        assertEquals("podcast", fakeApi.lastMedia)
    }

    private fun createITunesItem(
        id: Long,
        name: String,
        artist: String
    ): ITunesItem {
        return ITunesItem(
            trackId = id,
            collectionId = id,
            trackName = name,
            artistName = artist,
            artworkUrl100 = "https://example.com/artwork.jpg",
            trackPrice = 9.99,
            currency = "USD",
            primaryGenreName = "Music",
            releaseDate = "2024-01-01"
        )
    }
}

internal class FakeITunesApi : ITunesApi {
    var shouldFail = false
    var mockResults: List<ITunesItem> = emptyList()
    var lastMedia: String? = null
    var lastCountry: String? = null
    var lastLang: String? = null

    override suspend fun search(
        term: String,
        media: String?,
        entity: String?,
        attribute: String?,
        limit: Int,
        lang: String,
        country: String?
    ): ITunesSearchResponse {
        if (shouldFail) throw Exception("API error")

        lastMedia = media
        lastCountry = country
        lastLang = lang

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
        if (shouldFail) throw Exception("API error")

        lastCountry = country
        lastLang = lang

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
        sort: String,
        lang: String,
        country: String?
    ): ITunesSearchResponse {
        if (shouldFail) throw Exception("API error")

        return ITunesSearchResponse(
            resultCount = mockResults.size,
            results = mockResults
        )
    }
}
