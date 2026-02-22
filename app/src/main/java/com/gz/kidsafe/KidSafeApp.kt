package com.gz.kidsafe

import android.app.Application
import com.gz.kidsafe.ads.KidSafeAdsManager

class KidSafeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KidSafeAdsManager.initialize(applicationContext)
    }
}
