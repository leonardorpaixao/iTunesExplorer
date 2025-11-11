package com.itunesexplorer.network.models

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
)

enum class MediaType(val value: String) {
    ALL("all"),
    MOVIE("movie"),
    PODCAST("podcast"),
    MUSIC("music"),
    MUSIC_VIDEO("musicVideo"),
    AUDIOBOOK("audiobook"),
    SHORT_FILM("shortFilm"),
    TV_SHOW("tvShow"),
    SOFTWARE("software"),
    EBOOK("ebook")
}

// RSS Feed Models for Top Albums
@Serializable
data class ITunesRssResponse(
    val feed: RssFeed
)

@Serializable
data class RssFeed(
    val entry: List<RssFeedEntry>
)

@Serializable
data class RssFeedEntry(
    val id: RssId,
    @kotlinx.serialization.SerialName("im:name")
    val imName: RssLabel,
    @kotlinx.serialization.SerialName("im:image")
    val imImage: List<RssImage>,
    val title: RssLabel,
    val link: RssLink,
    val category: RssCategory,
    @kotlinx.serialization.SerialName("im:artist")
    val imArtist: RssArtist? = null,
    @kotlinx.serialization.SerialName("im:price")
    val imPrice: RssPrice? = null,
    @kotlinx.serialization.SerialName("im:releaseDate")
    val imReleaseDate: RssReleaseDate? = null
)

@Serializable
data class RssId(
    val label: String,
    val attributes: RssIdAttributes
) {
    @Serializable
    data class RssIdAttributes(
        @kotlinx.serialization.SerialName("im:id")
        val imId: String
    )
}

@Serializable
data class RssLabel(
    val label: String
)

@Serializable
data class RssImage(
    val label: String,
    val attributes: RssImageAttributes
) {
    @Serializable
    data class RssImageAttributes(
        val height: String
    )
}

@Serializable
data class RssLink(
    val attributes: RssLinkAttributes
) {
    @Serializable
    data class RssLinkAttributes(
        val href: String
    )
}

@Serializable
data class RssCategory(
    val attributes: RssCategoryAttributes
) {
    @Serializable
    data class RssCategoryAttributes(
        val label: String
    )
}

@Serializable
data class RssArtist(
    val label: String
)

@Serializable
data class RssPrice(
    val label: String,
    val attributes: RssPriceAttributes
) {
    @Serializable
    data class RssPriceAttributes(
        val amount: String,
        val currency: String
    )
}

@Serializable
data class RssReleaseDate(
    val attributes: RssReleaseDateAttributes
) {
    @Serializable
    data class RssReleaseDateAttributes(
        val label: String
    )
}
