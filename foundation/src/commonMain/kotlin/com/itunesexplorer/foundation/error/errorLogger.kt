package com.itunesexplorer.foundation.error

import com.itunesexplorer.core.logger.Logger
import com.itunesexplorer.core.logger.createPlatformLogger

internal val errorLogger: Logger by lazy {
    createPlatformLogger(com.itunesexplorer.core.logger.LogLevel.ERROR)
}