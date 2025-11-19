package com.itunesexplorer.catalog.data.models

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.Money
import com.itunesexplorer.catalog.domain.model.SearchResult
import kotlinx.serialization.Serializable

@Serializable
data class ITunesSearchResponse(
    val resultCount: Int,
    val results: List<ITunesItem>
)

@Serializable
data class ITunesItem(
    val wrapperType: String? = null,
    val kind: String? = null,
    val artistId: Long? = null,
    val collectionId: Long? = null,
    val trackId: Long? = null,
    val artistName: String? = null,
    val collectionName: String? = null,
    val trackName: String? = null,
    val collectionCensoredName: String? = null,
    val trackCensoredName: String? = null,
    val artistViewUrl: String? = null,
    val collectionViewUrl: String? = null,
    val trackViewUrl: String? = null,
    val previewUrl: String? = null,
    val artworkUrl30: String? = null,
    val artworkUrl60: String? = null,
    val artworkUrl100: String? = null,
    val collectionPrice: Double? = null,
    val trackPrice: Double? = null,
    val releaseDate: String? = null,
    val collectionExplicitness: String? = null,
    val trackExplicitness: String? = null,
    val discCount: Int? = null,
    val discNumber: Int? = null,
    val trackCount: Int? = null,
    val trackNumber: Int? = null,
    val trackTimeMillis: Long? = null,
    val country: String? = null,
    val currency: String? = null,
    val primaryGenreName: String? = null,
    val isStreamable: Boolean? = null,
    val contentAdvisoryRating: String? = null,
    val shortDescription: String? = null,
    val longDescription: String? = null
) {
    fun toSearchResult(): SearchResult {
        val id = trackId?.toString()
            ?: collectionId?.toString()
            ?: artistId?.toString()
            ?: CatalogConstants.UNKNOWN_ID

        val name = trackName ?: collectionName ?: artistName ?: CatalogConstants.UNKNOWN_NAME

        val imageUrl = artworkUrl100 ?: artworkUrl60 ?: artworkUrl30

        val viewUrl = trackViewUrl ?: collectionViewUrl ?: artistViewUrl

        val price = when {
            trackPrice != null && currency != null -> {
                Money.fromOptional(trackPrice, currency)
            }
            collectionPrice != null && currency != null -> {
                Money.fromOptional(collectionPrice, currency)
            }
            else -> null
        }

        val description = longDescription ?: shortDescription

        return SearchResult(
            id = id,
            type = kind ?: wrapperType ?: CatalogConstants.UNKNOWN_ID,
            name = name,
            artistName = artistName,
            collectionName = collectionName,
            imageUrl = imageUrl,
            viewUrl = viewUrl,
            previewUrl = previewUrl,
            price = price,
            releaseDate = releaseDate,
            genre = primaryGenreName,
            description = description
        )
    }
}
