package com.itunesexplorer.core.logger

/**
 * Multiplatform logger interface for consistent logging across all platforms.
 *
 * Usage:
 * ```kotlin
 * class MyViewModel(private val logger: Logger) {
 *     fun doSomething() {
 *         logger.debug("MyViewModel", "Doing something")
 *         try {
 *             // ...
 *         } catch (e: Exception) {
 *             logger.error("MyViewModel", "Failed to do something", e)
 *         }
 *     }
 * }
 * ```
 */
interface Logger {
    /**
     * Current log level. Logs below this level will not be printed.
     */
    val logLevel: LogLevel

    /**
     * Log a debug message.
     * @param tag Tag to identify the source of the log message
     * @param message The message to log
     */
    fun debug(tag: String, message: String)

    /**
     * Log an informational message.
     * @param tag Tag to identify the source of the log message
     * @param message The message to log
     */
    fun info(tag: String, message: String)

    /**
     * Log a warning message.
     * @param tag Tag to identify the source of the log message
     * @param message The message to log
     */
    fun warning(tag: String, message: String)

    /**
     * Log an error message.
     * @param tag Tag to identify the source of the log message
     * @param message The message to log
     * @param throwable Optional throwable to log
     */
    fun error(tag: String, message: String, throwable: Throwable? = null)
}

/**
 * Extension functions for easier logging with class names as tags.
 */
inline fun <reified T> T.debug(logger: Logger, message: String) {
    logger.debug(T::class.simpleName ?: "Unknown", message)
}

inline fun <reified T> T.info(logger: Logger, message: String) {
    logger.info(T::class.simpleName ?: "Unknown", message)
}

inline fun <reified T> T.warning(logger: Logger, message: String) {
    logger.warning(T::class.simpleName ?: "Unknown", message)
}

inline fun <reified T> T.error(logger: Logger, message: String, throwable: Throwable? = null) {
    logger.error(T::class.simpleName ?: "Unknown", message, throwable)
}
