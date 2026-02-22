package com.gz.kidsafe

import android.app.Application
import com.gz.kidsafe.ads.AgeSignal
import com.gz.kidsafe.ads.KidSafeAdsManager

class KidSafeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Keep app startup fail-closed until explicit age signal is available.
        KidSafeAdsManager.initialize(
            context = applicationContext,
            ageSignal = AgeSignal.UNKNOWN
        )
    }
}
