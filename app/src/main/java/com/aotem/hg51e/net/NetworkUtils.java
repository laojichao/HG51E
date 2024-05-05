package com.aotem.hg51e.net;

import com.aotem.hg51e.User;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/****
 *** author：lao
 *** package：com.aotem.hg51e.net
 *** project：HG51E
 *** name：NetworkUtils
 *** date：2024/5/5  2:07
 *** filename：NetworkUtils
 *** desc：
 ***/

public class NetworkUtils {

    private static ApiService apiService;

    private NetworkUtils(String host) {
        // Private constructor to prevent instantiation.
    }

    public static Observable<User> queryUser(String host) {
        apiService = RetrofitClient.getInstance(host).create(ApiService.class);
        return apiService.queryUserShow()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

