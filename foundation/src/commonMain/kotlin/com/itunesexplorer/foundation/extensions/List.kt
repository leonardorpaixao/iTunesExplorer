package com.itunesexplorer.foundation.extensions

fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()