package com.gz.kidsafe.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.gz.kidsafe.BuildConfig
import java.util.concurrent.atomic.AtomicBoolean

object KidSafeAdsManager {
    private const val TAG = "KidSafeAdsManager"
    private val initialized = AtomicBoolean(false)

    fun initialize(context: Context) {
        if (!BuildConfig.KIDSAFE_ADS_ENABLED) {
            Log.w(TAG, "Ads disabled by build config kill-switch")
            return
        }

        if (initialized.compareAndSet(false, true)) {
            MobileAds.setRequestConfiguration(AdPolicyConfig.requestConfiguration)
            MobileAds.initialize(context) {
                Log.i(TAG, "MobileAds initialized in kid-safe mode")
            }
        }
    }
}
