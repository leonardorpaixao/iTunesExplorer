package com.itunesexplorer.common.extensions

fun String?.orEmpty(): String = this ?: ""

fun String?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()
