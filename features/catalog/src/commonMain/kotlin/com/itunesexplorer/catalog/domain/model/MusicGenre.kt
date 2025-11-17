package com.itunesexplorer.catalog.domain.model

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
