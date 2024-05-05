package com.aotem.hg51e.net;

import com.aotem.hg51e.User;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

/****
*** author：lao
*** package：com.aotem.hg51e.net
*** project：HG51E
*** name：ApiService
*** date：2024/5/5  1:54
*** filename：ApiService
*** desc：
***/
    
public interface ApiService {
    @GET("/boaform/web_query_user_show.cgi")
    Observable<User> queryUserShow();
}
