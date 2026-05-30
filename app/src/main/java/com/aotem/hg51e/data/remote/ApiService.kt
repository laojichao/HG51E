package com.aotem.hg51e.data.remote

import com.aotem.hg51e.data.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("/boaform/web_query_user_show.cgi")
    suspend fun queryUserShow(): User
}
