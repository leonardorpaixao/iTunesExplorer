package com.itunesexplorer.core.network

import kotlinx.serialization.json.Json

fun createJsonConfig(): Json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
}
