package com.itunesexplorer.home.i18n

import cafe.adriel.lyricist.LyricistStrings
import com.itunesexplorer.i18n.Locales
import com.itunesexplorer.network.models.MediaType

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnHomeStrings = HomeStrings(
    appName = "iTunes Explorer",
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
    }
)

@LyricistStrings(languageTag = Locales.PT_BR, default = false)
val PtBrHomeStrings = HomeStrings(
    appName = "iTunes Explorer",
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
    }
)

@LyricistStrings(languageTag = Locales.PT_PT, default = false)
val PtPtHomeStrings = HomeStrings(
    appName = "iTunes Explorer",
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
    }
)

@LyricistStrings(languageTag = Locales.FR, default = false)
val FrHomeStrings = HomeStrings(
    appName = "iTunes Explorer",
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
    }
)

@LyricistStrings(languageTag = Locales.ES, default = false)
val EsHomeStrings = HomeStrings(
    appName = "iTunes Explorer",
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
    }
)

@LyricistStrings(languageTag = Locales.DE, default = false)
val DeHomeStrings = HomeStrings(
    appName = "iTunes Explorer",
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
    }
)
