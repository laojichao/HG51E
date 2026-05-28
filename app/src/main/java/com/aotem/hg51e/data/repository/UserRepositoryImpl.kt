package com.aotem.hg51e.data.repository

import com.aotem.hg51e.data.model.User
import com.aotem.hg51e.data.remote.ApiService
import com.aotem.hg51e.data.remote.RetrofitClient
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import javax.inject.Inject

interface UserRepository {
    fun queryUser(host: String): Observable<User>
}

class UserRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : UserRepository {

    override fun queryUser(host: String): Observable<User> {
        val service = RetrofitClient.getInstance(host, okHttpClient)
            .create(ApiService::class.java)
        return service.queryUserShow()
    }
}
