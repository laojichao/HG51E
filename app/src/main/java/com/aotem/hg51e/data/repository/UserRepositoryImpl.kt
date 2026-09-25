package com.aotem.hg51e.data.repository

import com.aotem.hg51e.data.model.User
import com.aotem.hg51e.data.remote.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

interface UserRepository {
    suspend fun queryUser(host: String): User
}

class UserRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : UserRepository {

    private val lock = Any()
    private val services = mutableMapOf<String, ApiService>()

    override suspend fun queryUser(host: String): User {
        val service = synchronized(lock) {
            services.getOrPut(host) { createService(host) }
        }
        return service.queryUserShow()
    }

    private fun createService(host: String): ApiService =
        Retrofit.Builder()
            .baseUrl(host.trimEnd('/') + "/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
}
