package com.itunesexplorer.catalog.presentation

import com.itunesexplorer.core.error.DomainError

/**
 * Extension to extract error message from DomainError.
 * Message formatting and i18n should be handled at the presentation layer.
 */
internal fun DomainError.toMessage(): String = when (this) {
    is DomainError.NetworkError -> message
    is DomainError.ServerError -> message
    is DomainError.NotFoundError -> message
    is DomainError.ClientError -> message
    is DomainError.DataError -> message
    is DomainError.UnknownError -> message
}
