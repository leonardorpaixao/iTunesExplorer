package com.itunesexplorer.core.logger

import android.util.Log

/**
 * Android implementation of Logger using android.util.Log.
 */
internal class AndroidLogger(
    override val logLevel: LogLevel
) : Logger {

    override fun debug(tag: String, message: String) {
        if (shouldLog(LogLevel.DEBUG)) {
            Log.d(tag, message)
        }
    }

    override fun info(tag: String, message: String) {
        if (shouldLog(LogLevel.INFO)) {
            Log.i(tag, message)
        }
    }

    override fun warning(tag: String, message: String) {
        if (shouldLog(LogLevel.WARNING)) {
            Log.w(tag, message)
        }
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        if (shouldLog(LogLevel.ERROR)) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    private fun shouldLog(level: LogLevel): Boolean {
        return level.ordinal >= logLevel.ordinal && logLevel != LogLevel.NONE
    }
}

/**
 * Creates an Android-specific logger using Logcat.
 */
actual fun createPlatformLogger(logLevel: LogLevel): Logger {
    return AndroidLogger(logLevel)
}
