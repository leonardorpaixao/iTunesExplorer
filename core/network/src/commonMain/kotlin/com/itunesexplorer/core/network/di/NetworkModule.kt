package com.itunesexplorer.core.network.di

import com.itunesexplorer.core.logger.Logger
import com.itunesexplorer.core.network.createJsonConfig
import com.itunesexplorer.core.network.createPlatformHttpClient
import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

/**
 * Kodein DI module for network configuration.
 *
 * Provides:
 * - Json: Configured serializer for HTTP requests/responses
 * - HttpClient: Platform-specific HTTP client (OkHttp, Darwin, or CIO)
 *
 * Dependencies:
 * - Logger: Required for configurable HTTP logging
 */
val networkModule = DI.Module("networkModule") {
    bindSingleton<Json> {
        createJsonConfig()
    }

    bindSingleton<HttpClient> {
        createPlatformHttpClient(
            json = instance(),
            logger = instance<Logger>()
        )
    }
}
