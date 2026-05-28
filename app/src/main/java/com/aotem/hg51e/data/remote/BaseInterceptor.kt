package com.aotem.hg51e.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class BaseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json;charset=UTF-8")
            .build()
        return chain.proceed(request)
    }
}
