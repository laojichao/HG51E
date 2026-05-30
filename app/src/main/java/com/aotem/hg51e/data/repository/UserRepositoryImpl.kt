package com.aotem.hg51e.data.repository

import com.aotem.hg51e.data.model.User
import com.aotem.hg51e.data.remote.ApiService
import com.aotem.hg51e.data.remote.RetrofitClient
import okhttp3.OkHttpClient
import javax.inject.Inject

interface UserRepository {
    suspend fun queryUser(host: String): User
}

class UserRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : UserRepository {

    private var cachedService: ApiService? = null
    private var cachedHost: String? = null

    override suspend fun queryUser(host: String): User {
        val service = if (cachedService != null && host == cachedHost) {
            cachedService!!
        } else {
            RetrofitClient.getInstance(host, okHttpClient)
                .create(ApiService::class.java).also {
                    cachedService = it
                    cachedHost = host
                }
        }
        return service.queryUserShow()
    }
}
