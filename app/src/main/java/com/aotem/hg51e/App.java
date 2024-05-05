package com.aotem.hg51e;

import android.app.Application;

import com.hjq.toast.Toaster;

/****
*** author：lao
*** package：com.aotem.hg51e
*** project：HG51E
*** name：App
*** date：2024/5/5  2:34
*** filename：App
*** desc：
***/
    
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 Toast 框架
        Toaster.init(this);
    }
}
