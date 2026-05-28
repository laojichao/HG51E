package com.aotem.hg51e.data.remote

import com.aotem.hg51e.data.model.User
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface ApiService {
    @GET("/boaform/web_query_user_show.cgi")
    fun queryUserShow(): Observable<User>
}
