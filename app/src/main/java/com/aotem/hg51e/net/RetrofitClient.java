package com.aotem.hg51e.net;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/****
*** author：lao
*** package：com.aotem.hg51e.net
*** project：HG51E
*** name：RetrofitClient
*** date：2024/5/5  2:00
*** filename：RetrofitClient
*** desc：
***/

public class RetrofitClient {
    private static Retrofit retrofit;
    private static OkHttpClient mOkHttpClient;

    private static final int TIMEOUT_CONNECT = 10;// 连接超时时间（秒）
    private static final int TIMEOUT_READ = 30;// 读取超时时间（秒）
    private static final int TIMEOUT_WRITE = 30; // 写入超时时间（秒）
    private static final int MAX_IDLE_CONNECTIONS = 8; // 最大空闲连接数
    private static final int KEEP_ALIVE_DURATION = 30;// 空闲连接保持时间（秒）
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS; // 空闲连接保持时间单位
    private static HashMap<String, String> headMap = new HashMap<>();//头部信息
    private RetrofitClient() {
        // Private constructor to prevent instantiation.
    }


    /**
     * 获取 OkHttpClient 实例
     * @return OkHttpClient 实例
     */
    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            //使用连接池，尽量保持TCP连接的复用，而不是立即关闭。
            ConnectionPool connectionPool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, KEEP_ALIVE_TIME_UNIT);
            Map<String, String> headerMap;
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
                    .connectionPool(connectionPool)
                    .proxy(Proxy.NO_PROXY)
                    .addInterceptor(new BaseInterceptor(headMap))//添加头部信息
                    .build();
        }
        return mOkHttpClient;
    }

    public static Retrofit getInstance(String host) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(host)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
