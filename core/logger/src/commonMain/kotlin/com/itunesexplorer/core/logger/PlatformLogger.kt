package com.itunesexplorer.core.logger

/**
 * Factory function to create a platform-specific logger implementation.
 * @param logLevel The minimum log level to print
 */
expect fun createPlatformLogger(logLevel: LogLevel = LogLevel.DEBUG): Logger
