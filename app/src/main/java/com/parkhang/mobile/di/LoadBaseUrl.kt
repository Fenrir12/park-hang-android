package com.parkhang.mobile.di

class LoadBaseUrl(
    private val loadBaseUrl: suspend () -> String,
) {
    suspend operator fun invoke(): String = loadBaseUrl()
}
