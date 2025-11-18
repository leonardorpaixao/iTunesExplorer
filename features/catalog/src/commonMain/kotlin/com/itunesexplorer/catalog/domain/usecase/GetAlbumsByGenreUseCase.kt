package com.itunesexplorer.catalog.domain.usecase

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.catalog.domain.model.MusicGenre
import com.itunesexplorer.core.error.DomainResult

/**
 * Use case interface for fetching albums filtered by genre from the iTunes Search API.
 * Note: RSS feed doesn't support genre filtering, so this uses the Search API.
 */
interface GetAlbumsByGenreUseCase {
    /**
     * Fetches albums filtered by a specific music genre.
     *
     * @param genre The music genre to filter by
     * @param limit Maximum number of albums to fetch
     * @return DomainResult containing list of albums or a domain error
     */
    suspend operator fun invoke(
        genre: MusicGenre,
        limit: Int = CatalogConstants.REQUEST_ITEMS_LIMIT
    ): DomainResult<List<Album>>
}
