package com.itunesexplorer.core.logger

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * ANSI color codes for console output.
 */
private object AnsiColors {
    const val RESET = "\u001B[0m"
    const val BLACK = "\u001B[30m"
    const val RED = "\u001B[31m"
    const val GREEN = "\u001B[32m"
    const val YELLOW = "\u001B[33m"
    const val BLUE = "\u001B[34m"
    const val PURPLE = "\u001B[35m"
    const val CYAN = "\u001B[36m"
    const val WHITE = "\u001B[37m"

    const val BOLD = "\u001B[1m"
}

/**
 * Desktop/JVM implementation of Logger with colored console output.
 */
internal class DesktopLogger(
    override val logLevel: LogLevel
) : Logger {

    override fun debug(tag: String, message: String) {
        if (shouldLog(LogLevel.DEBUG)) {
            log(AnsiColors.CYAN, "DEBUG", tag, message)
        }
    }

    override fun info(tag: String, message: String) {
        if (shouldLog(LogLevel.INFO)) {
            log(AnsiColors.GREEN, "INFO", tag, message)
        }
    }

    override fun warning(tag: String, message: String) {
        if (shouldLog(LogLevel.WARNING)) {
            log(AnsiColors.YELLOW, "WARNING", tag, message)
        }
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        if (shouldLog(LogLevel.ERROR)) {
            val errorMessage = if (throwable != null) {
                buildString {
                    append(message)
                    append("\n")
                    append("${throwable::class.simpleName}: ${throwable.message}")
                    append("\n")
                    append(throwable.stackTraceToString())
                }
            } else {
                message
            }
            log(AnsiColors.RED, "ERROR", tag, errorMessage)
        }
    }

    private fun log(color: String, level: String, tag: String, message: String) {
        val timestamp = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toString()
            .substringBeforeLast('.')

        println("${AnsiColors.BOLD}$timestamp${AnsiColors.RESET} $color[$level]${AnsiColors.RESET} ${AnsiColors.BOLD}[$tag]${AnsiColors.RESET} $message")
    }

    private fun shouldLog(level: LogLevel): Boolean {
        return level.ordinal >= logLevel.ordinal && logLevel != LogLevel.NONE
    }
}

/**
 * Creates a Desktop-specific logger with colored console output.
 */
actual fun createPlatformLogger(logLevel: LogLevel): Logger {
    return DesktopLogger(logLevel)
}
