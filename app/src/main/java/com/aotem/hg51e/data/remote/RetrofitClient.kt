package com.aotem.hg51e.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    @Volatile
    private var retrofit: Retrofit? = null
    private var cachedHost: String? = null
    private var cachedClient: OkHttpClient? = null

    fun getInstance(host: String, client: OkHttpClient): Retrofit {
        return retrofit ?: synchronized(this) {
            if (retrofit == null || host != cachedHost || client !== cachedClient) {
                cachedHost = host
                cachedClient = client
                retrofit = Retrofit.Builder()
                    .baseUrl(host)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            retrofit!!
        }
    }
}
