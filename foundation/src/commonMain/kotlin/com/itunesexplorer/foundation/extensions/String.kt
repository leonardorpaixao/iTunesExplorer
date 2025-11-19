package com.itunesexplorer.foundation.extensions

fun String?.orEmpty(): String = this ?: ""

fun String?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()


