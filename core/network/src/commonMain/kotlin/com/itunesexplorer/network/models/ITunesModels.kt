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
    PODCAST("podcast"),
    MUSIC("music"),
    MUSIC_VIDEO("musicVideo"),
    AUDIOBOOK("audiobook"),
    SHORT_FILM("shortFilm"),
    TV_SHOW("tvShow"),
    SOFTWARE("software"),
    EBOOK("ebook")
}

enum class MusicGenre(val searchTerm: String, val genreName: String) {
    ALL("top albums", "All"),
    ROCK("rock", "Rock"),
    POP("pop", "Pop"),
    JAZZ("jazz", "Jazz"),
    BLUES("blues", "Blues"),
    CLASSICAL("classical", "Classical"),
    HIP_HOP_RAP("hip hop", "Hip-Hop/Rap"),
    ELECTRONIC("electronic", "Electronic"),
    COUNTRY("country", "Country"),
    R_B_SOUL("r&b soul", "R&B/Soul"),
    ALTERNATIVE("alternative", "Alternative"),
    METAL("metal", "Metal"),
    INDIE("indie", "Indie Rock")
}
