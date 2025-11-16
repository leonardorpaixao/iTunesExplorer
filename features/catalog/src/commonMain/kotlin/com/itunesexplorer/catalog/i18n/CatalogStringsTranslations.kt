package com.itunesexplorer.catalog.i18n

import cafe.adriel.lyricist.LyricistStrings
import com.itunesexplorer.i18n.Locales
import com.itunesexplorer.network.models.MediaType
import com.itunesexplorer.network.models.MusicGenre

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnCatalogStrings = CatalogStrings(
    search = "Search",
    searchPlaceholder = "Search for music, movies, apps...",
    noResults = "No results found",
    errorLoading = "Error loading content",
    retry = "Retry",
    topAlbums = "Top Albums",
    topAlbumsDescription = "The top trending albums",
    surpriseMe = "Surprise Me",
    mediaTypeChip = { mediaType ->
        when(mediaType) {
            MediaType.ALL -> "All"
            MediaType.MOVIE -> "Movie"
            MediaType.PODCAST -> "Podcast"
            MediaType.MUSIC -> "Music"
            MediaType.MUSIC_VIDEO -> "Music Video"
            MediaType.AUDIOBOOK -> "Audiobook"
            MediaType.SHORT_FILM -> "Short Film"
            MediaType.TV_SHOW -> "TV Show"
            MediaType.SOFTWARE -> "Software"
            MediaType.EBOOK -> "eBook"
        }
    },
    musicGenreChip = { genre ->
        when(genre) {
            MusicGenre.ALL -> "All"
            MusicGenre.ROCK -> "Rock"
            MusicGenre.POP -> "Pop"
            MusicGenre.JAZZ -> "Jazz"
            MusicGenre.BLUES -> "Blues"
            MusicGenre.CLASSICAL -> "Classical"
            MusicGenre.HIP_HOP_RAP -> "Hip-Hop/Rap"
            MusicGenre.ELECTRONIC -> "Electronic"
            MusicGenre.COUNTRY -> "Country"
            MusicGenre.R_B_SOUL -> "R&B/Soul"
            MusicGenre.ALTERNATIVE -> "Alternative"
            MusicGenre.METAL -> "Metal"
            MusicGenre.INDIE -> "Indie"
        }
    },
    details = "Details",
    noDetails = "No details available",
    relatedItems = "Related Items",
    genre = "Genre",
    price = "Price",
    releaseDate = "Release Date",
    trackCount = "Tracks",
    openInStore = "Open in iTunes Store"
)

@LyricistStrings(languageTag = Locales.PT_BR, default = false)
val PtBrCatalogStrings = CatalogStrings(
    search = "Pesquisar",
    searchPlaceholder = "Pesquisar músicas, filmes, apps...",
    noResults = "Nenhum resultado encontrado",
    errorLoading = "Erro ao carregar conteúdo",
    retry = "Tentar novamente",
    topAlbums = "Top Álbuns",
    topAlbumsDescription = "Os principais álbuns em alta",
    surpriseMe = "Surpreenda-me",
    mediaTypeChip = { mediaType ->
        when(mediaType) {
            MediaType.ALL -> "Todos"
            MediaType.MOVIE -> "Filme"
            MediaType.PODCAST -> "Podcast"
            MediaType.MUSIC -> "Música"
            MediaType.MUSIC_VIDEO -> "Vídeo Musical"
            MediaType.AUDIOBOOK -> "Audiolivro"
            MediaType.SHORT_FILM -> "Curta-metragem"
            MediaType.TV_SHOW -> "Programa de TV"
            MediaType.SOFTWARE -> "Software"
            MediaType.EBOOK -> "eBook"
        }
    },
    musicGenreChip = { genre ->
        when(genre) {
            MusicGenre.ALL -> "Todos"
            MusicGenre.ROCK -> "Rock"
            MusicGenre.POP -> "Pop"
            MusicGenre.JAZZ -> "Jazz"
            MusicGenre.BLUES -> "Blues"
            MusicGenre.CLASSICAL -> "Clássica"
            MusicGenre.HIP_HOP_RAP -> "Hip-Hop/Rap"
            MusicGenre.ELECTRONIC -> "Eletrônica"
            MusicGenre.COUNTRY -> "Country"
            MusicGenre.R_B_SOUL -> "R&B/Soul"
            MusicGenre.ALTERNATIVE -> "Alternativo"
            MusicGenre.METAL -> "Metal"
            MusicGenre.INDIE -> "Indie"
        }
    },
    details = "Detalhes",
    noDetails = "Nenhum detalhe disponível",
    relatedItems = "Itens Relacionados",
    genre = "Gênero",
    price = "Preço",
    releaseDate = "Data de Lançamento",
    trackCount = "Faixas",
    openInStore = "Abrir na iTunes Store"
)

@LyricistStrings(languageTag = Locales.PT_PT, default = false)
val PtPtCatalogStrings = CatalogStrings(
    search = "Pesquisar",
    searchPlaceholder = "Pesquisar música, filmes, aplicações...",
    noResults = "Nenhum resultado encontrado",
    errorLoading = "Erro ao carregar conteúdo",
    retry = "Tentar novamente",
    topAlbums = "Principais Álbuns",
    topAlbumsDescription = "Os principais álbuns em destaque",
    surpriseMe = "Surpreende-me",
    mediaTypeChip = { mediaType ->
        when(mediaType) {
            MediaType.ALL -> "Todos"
            MediaType.MOVIE -> "Filme"
            MediaType.PODCAST -> "Podcast"
            MediaType.MUSIC -> "Música"
            MediaType.MUSIC_VIDEO -> "Vídeo Musical"
            MediaType.AUDIOBOOK -> "Audiolivro"
            MediaType.SHORT_FILM -> "Curta-metragem"
            MediaType.TV_SHOW -> "Programa de TV"
            MediaType.SOFTWARE -> "Software"
            MediaType.EBOOK -> "eBook"
        }
    },
    musicGenreChip = { genre ->
        when(genre) {
            MusicGenre.ALL -> "Todos"
            MusicGenre.ROCK -> "Rock"
            MusicGenre.POP -> "Pop"
            MusicGenre.JAZZ -> "Jazz"
            MusicGenre.BLUES -> "Blues"
            MusicGenre.CLASSICAL -> "Clássica"
            MusicGenre.HIP_HOP_RAP -> "Hip-Hop/Rap"
            MusicGenre.ELECTRONIC -> "Electrónica"
            MusicGenre.COUNTRY -> "Country"
            MusicGenre.R_B_SOUL -> "R&B/Soul"
            MusicGenre.ALTERNATIVE -> "Alternativo"
            MusicGenre.METAL -> "Metal"
            MusicGenre.INDIE -> "Indie"
        }
    },
    details = "Detalhes",
    noDetails = "Nenhum detalhe disponível",
    relatedItems = "Itens Relacionados",
    genre = "Género",
    price = "Preço",
    releaseDate = "Data de Lançamento",
    trackCount = "Faixas",
    openInStore = "Abrir na iTunes Store"
)

@LyricistStrings(languageTag = Locales.FR, default = false)
val FrCatalogStrings = CatalogStrings(
    search = "Rechercher",
    searchPlaceholder = "Rechercher musique, films, apps...",
    noResults = "Aucun résultat trouvé",
    errorLoading = "Erreur lors du chargement du contenu",
    retry = "Réessayer",
    topAlbums = "Meilleurs Albums",
    topAlbumsDescription = "Les albums les plus populaires",
    surpriseMe = "Surprenez-moi",
    mediaTypeChip = { mediaType ->
        when(mediaType) {
            MediaType.ALL -> "Tout"
            MediaType.MOVIE -> "Film"
            MediaType.PODCAST -> "Podcast"
            MediaType.MUSIC -> "Musique"
            MediaType.MUSIC_VIDEO -> "Clip musical"
            MediaType.AUDIOBOOK -> "Livre audio"
            MediaType.SHORT_FILM -> "Court métrage"
            MediaType.TV_SHOW -> "Série TV"
            MediaType.SOFTWARE -> "Logiciel"
            MediaType.EBOOK -> "eBook"
        }
    },
    musicGenreChip = { genre ->
        when(genre) {
            MusicGenre.ALL -> "Tout"
            MusicGenre.ROCK -> "Rock"
            MusicGenre.POP -> "Pop"
            MusicGenre.JAZZ -> "Jazz"
            MusicGenre.BLUES -> "Blues"
            MusicGenre.CLASSICAL -> "Classique"
            MusicGenre.HIP_HOP_RAP -> "Hip-Hop/Rap"
            MusicGenre.ELECTRONIC -> "Électronique"
            MusicGenre.COUNTRY -> "Country"
            MusicGenre.R_B_SOUL -> "R&B/Soul"
            MusicGenre.ALTERNATIVE -> "Alternatif"
            MusicGenre.METAL -> "Metal"
            MusicGenre.INDIE -> "Indie"
        }
    },
    details = "Détails",
    noDetails = "Aucun détail disponible",
    relatedItems = "Articles Connexes",
    genre = "Genre",
    price = "Prix",
    releaseDate = "Date de sortie",
    trackCount = "Pistes",
    openInStore = "Ouvrir dans iTunes Store"
)

@LyricistStrings(languageTag = Locales.ES, default = false)
val EsCatalogStrings = CatalogStrings(
    search = "Buscar",
    searchPlaceholder = "Buscar música, películas, apps...",
    noResults = "No se encontraron resultados",
    errorLoading = "Error al cargar el contenido",
    retry = "Reintentar",
    topAlbums = "Mejores Álbumes",
    topAlbumsDescription = "Los álbumes más populares",
    surpriseMe = "Sorpréndeme",
    mediaTypeChip = { mediaType ->
        when(mediaType) {
            MediaType.ALL -> "Todos"
            MediaType.MOVIE -> "Película"
            MediaType.PODCAST -> "Podcast"
            MediaType.MUSIC -> "Música"
            MediaType.MUSIC_VIDEO -> "Vídeo musical"
            MediaType.AUDIOBOOK -> "Audiolibro"
            MediaType.SHORT_FILM -> "Cortometraje"
            MediaType.TV_SHOW -> "Programa de TV"
            MediaType.SOFTWARE -> "Software"
            MediaType.EBOOK -> "eBook"
        }
    },
    musicGenreChip = { genre ->
        when(genre) {
            MusicGenre.ALL -> "Todos"
            MusicGenre.ROCK -> "Rock"
            MusicGenre.POP -> "Pop"
            MusicGenre.JAZZ -> "Jazz"
            MusicGenre.BLUES -> "Blues"
            MusicGenre.CLASSICAL -> "Clásica"
            MusicGenre.HIP_HOP_RAP -> "Hip-Hop/Rap"
            MusicGenre.ELECTRONIC -> "Electrónica"
            MusicGenre.COUNTRY -> "Country"
            MusicGenre.R_B_SOUL -> "R&B/Soul"
            MusicGenre.ALTERNATIVE -> "Alternativo"
            MusicGenre.METAL -> "Metal"
            MusicGenre.INDIE -> "Indie"
        }
    },
    details = "Detalles",
    noDetails = "No hay detalles disponibles",
    relatedItems = "Artículos Relacionados",
    genre = "Género",
    price = "Precio",
    releaseDate = "Fecha de Lanzamiento",
    trackCount = "Pistas",
    openInStore = "Abrir en iTunes Store"
)

@LyricistStrings(languageTag = Locales.DE, default = false)
val DeCatalogStrings = CatalogStrings(
    search = "Suchen",
    searchPlaceholder = "Musik, Filme, Apps suchen...",
    noResults = "Keine Ergebnisse gefunden",
    errorLoading = "Fehler beim Laden des Inhalts",
    retry = "Erneut versuchen",
    topAlbums = "Top-Alben",
    topAlbumsDescription = "Die beliebtesten Alben",
    surpriseMe = "Überrasche mich",
    mediaTypeChip = { mediaType ->
        when(mediaType) {
            MediaType.ALL -> "Alle"
            MediaType.MOVIE -> "Film"
            MediaType.PODCAST -> "Podcast"
            MediaType.MUSIC -> "Musik"
            MediaType.MUSIC_VIDEO -> "Musikvideo"
            MediaType.AUDIOBOOK -> "Hörbuch"
            MediaType.SHORT_FILM -> "Kurzfilm"
            MediaType.TV_SHOW -> "TV-Sendung"
            MediaType.SOFTWARE -> "Software"
            MediaType.EBOOK -> "eBook"
        }
    },
    musicGenreChip = { genre ->
        when(genre) {
            MusicGenre.ALL -> "Alle"
            MusicGenre.ROCK -> "Rock"
            MusicGenre.POP -> "Pop"
            MusicGenre.JAZZ -> "Jazz"
            MusicGenre.BLUES -> "Blues"
            MusicGenre.CLASSICAL -> "Klassik"
            MusicGenre.HIP_HOP_RAP -> "Hip-Hop/Rap"
            MusicGenre.ELECTRONIC -> "Elektronisch"
            MusicGenre.COUNTRY -> "Country"
            MusicGenre.R_B_SOUL -> "R&B/Soul"
            MusicGenre.ALTERNATIVE -> "Alternative"
            MusicGenre.METAL -> "Metal"
            MusicGenre.INDIE -> "Indie"
        }
    },
    details = "Details",
    noDetails = "Keine Details verfügbar",
    relatedItems = "Verwandte Artikel",
    genre = "Genre",
    price = "Preis",
    releaseDate = "Veröffentlichungsdatum",
    trackCount = "Titel",
    openInStore = "Im iTunes Store öffnen"
)
