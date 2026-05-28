package com.aotem.hg51e.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null
    private var cachedHost: String? = null
    private var cachedClient: OkHttpClient? = null

    fun getInstance(host: String, client: OkHttpClient): Retrofit {
        if (retrofit == null || host != cachedHost || client !== cachedClient) {
            cachedHost = host
            cachedClient = client
            retrofit = Retrofit.Builder()
                .baseUrl(host)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
