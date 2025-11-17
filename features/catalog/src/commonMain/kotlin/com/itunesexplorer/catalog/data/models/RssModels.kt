package com.itunesexplorer.catalog.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// RSS Feed Models for Top Albums
@Serializable
data class ITunesRssResponse(
    val feed: RssFeed
)

@Serializable
data class RssFeed(
    val entry: List<RssFeedEntry> = emptyList()
)

@Serializable
data class RssFeedEntry(
    val id: RssId,
    @SerialName("im:name")
    val imName: RssLabel,
    @SerialName("im:image")
    val imImage: List<RssImage>,
    val title: RssLabel,
    val link: RssLink,
    val category: RssCategory,
    @SerialName("im:artist")
    val imArtist: RssArtist? = null,
    @SerialName("im:price")
    val imPrice: RssPrice? = null,
    @SerialName("im:releaseDate")
    val imReleaseDate: RssReleaseDate? = null
)

@Serializable
data class RssId(
    val label: String,
    val attributes: RssIdAttributes
) {
    @Serializable
    data class RssIdAttributes(
        @SerialName("im:id")
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
