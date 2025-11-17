package com.itunesexplorer.core.logger

/**
 * Log levels for the logger.
 * Levels are ordered from most verbose (DEBUG) to least verbose (NONE).
 */
enum class LogLevel {
    /**
     * Debug logs for detailed troubleshooting.
     * Should only be used in development builds.
     */
    DEBUG,

    /**
     * Informational logs for tracking application flow.
     */
    INFO,

    /**
     * Warning logs for potentially harmful situations.
     */
    WARNING,

    /**
     * Error logs for error events that might still allow the app to continue running.
     */
    ERROR,

    /**
     * Disables all logging.
     */
    NONE
}
