package com.itunesexplorer.core.network

import com.itunesexplorer.core.logger.Logger
import io.ktor.client.*
import kotlinx.serialization.json.Json

/**
 * Platform-specific HTTP client configuration.
 *
 * Each platform provides its own implementation:
 * - Android: OkHttp engine with content encoding support
 * - iOS: Darwin engine with Content-Length workaround
 * - Desktop/JVM: CIO engine for multiplatform compatibility
 *
 * @param json The Json serializer configuration
 * @param logger The Logger instance for logging configuration
 * @return A configured HttpClient instance for the current platform
 */
expect fun createPlatformHttpClient(json: Json, logger: Logger): HttpClient
