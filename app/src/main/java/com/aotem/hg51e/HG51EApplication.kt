package com.aotem.hg51e

import android.app.Application
import com.hjq.toast.Toaster
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HG51EApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Toaster.init(this)
    }
}
