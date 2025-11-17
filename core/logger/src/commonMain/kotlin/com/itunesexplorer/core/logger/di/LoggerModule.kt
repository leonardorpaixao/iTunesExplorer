package com.itunesexplorer.core.logger.di

import com.itunesexplorer.core.logger.LogLevel
import com.itunesexplorer.core.logger.Logger
import com.itunesexplorer.core.logger.createPlatformLogger
import org.kodein.di.DI
import org.kodein.di.bindSingleton

/**
 * Kodein DI module for Logger.
 *
 * Provides a singleton Logger instance that can be injected throughout the application.
 *
 * Usage in other modules:
 * ```kotlin
 * class MyViewModel(private val logger: Logger) : MviViewModel(...) {
 *     init {
 *         logger.debug("MyViewModel", "ViewModel initialized")
 *     }
 * }
 * ```
 *
 * To change the log level, modify the `createPlatformLogger()` call below.
 */
val loggerModule = DI.Module("loggerModule") {
    bindSingleton<Logger> {
        // Change LogLevel here to adjust logging verbosity
        // LogLevel.DEBUG - Most verbose (for development)
        // LogLevel.INFO - Informational messages
        // LogLevel.WARNING - Warnings only
        // LogLevel.ERROR - Errors only
        // LogLevel.NONE - Disable all logging (for production)
        createPlatformLogger(LogLevel.DEBUG)
    }
}
