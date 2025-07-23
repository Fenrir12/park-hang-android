package com.parkhang.mobile.framework.network.client.config

import okhttp3.OkHttpClient

class BaseHttpEngine {
    fun build(): OkHttpClient {
        val instance = OkHttpClient.Builder()

        return instance.build()
    }
}
