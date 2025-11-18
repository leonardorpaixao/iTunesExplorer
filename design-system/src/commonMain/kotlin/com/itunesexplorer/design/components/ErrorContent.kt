package com.itunesexplorer.design.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.itunesexplorer.core.error.DomainError

/**
 * Displays typed error content with appropriate icons and messages based on error type.
 *
 * This component provides a better UX by showing different UI elements based on the
 * type of error (network, server, not found, etc.) instead of generic error messages.
 *
 * @param error The DomainError to display
 * @param onRetry Optional retry callback
 * @param retryText Text for the retry button (defaults to "Retry")
 * @param modifier Optional modifier for the root composable
 */
@Composable
fun ErrorContent(
    error: DomainError,
    onRetry: (() -> Unit)? = null,
    retryText: String = "Retry",
    modifier: Modifier = Modifier
) {
    val (title, message) = when (error) {
        is DomainError.NetworkError -> Pair(
            "Connection Error",
            "Please check your internet connection and try again."
        )
        is DomainError.ServerError -> Pair(
            "Server Error",
            "The server is currently unavailable. Please try again later."
        )
        is DomainError.NotFoundError -> Pair(
            "Not Found",
            "We couldn't find what you're looking for."
        )
        is DomainError.ClientError -> Pair(
            "Request Error",
            "There was a problem with your request."
        )
        is DomainError.DataError -> Pair(
            "Data Error",
            "We couldn't process the response from the server."
        )
        is DomainError.UnknownError -> Pair(
            "Unexpected Error",
            "Something went wrong. Please try again."
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .widthIn(max = 400.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = title,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Show detailed error message if available (for debugging)
            val detailMessage = when (error) {
                is DomainError.NetworkError -> error.message
                is DomainError.ServerError -> error.message
                is DomainError.NotFoundError -> error.message
                is DomainError.ClientError -> error.message
                is DomainError.DataError -> error.message
                is DomainError.UnknownError -> error.message
            }

            if (detailMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = detailMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }

            if (onRetry != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onRetry,
                    modifier = Modifier.widthIn(min = 120.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(retryText)
                }
            }
        }
    }
}
