package com.itunesexplorer.core.logger

import kotlinx.datetime.Clock
import platform.Foundation.NSLog

/**
 * iOS implementation of Logger using NSLog.
 */
internal class IosLogger(
    override val logLevel: LogLevel
) : Logger {

    override fun debug(tag: String, message: String) {
        if (shouldLog(LogLevel.DEBUG)) {
            NSLog("[DEBUG] [$tag] $message")
        }
    }

    override fun info(tag: String, message: String) {
        if (shouldLog(LogLevel.INFO)) {
            NSLog("[INFO] [$tag] $message")
        }
    }

    override fun warning(tag: String, message: String) {
        if (shouldLog(LogLevel.WARNING)) {
            NSLog("[WARNING] [$tag] $message")
        }
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        if (shouldLog(LogLevel.ERROR)) {
            val errorMessage = if (throwable != null) {
                "$message - ${throwable::class.simpleName}: ${throwable.message}\n${throwable.stackTraceToString()}"
            } else {
                message
            }
            NSLog("[ERROR] [$tag] $errorMessage")
        }
    }

    private fun shouldLog(level: LogLevel): Boolean {
        return level.ordinal >= logLevel.ordinal && logLevel != LogLevel.NONE
    }
}

/**
 * Creates an iOS-specific logger using NSLog.
 */
actual fun createPlatformLogger(logLevel: LogLevel): Logger {
    return IosLogger(logLevel)
}
