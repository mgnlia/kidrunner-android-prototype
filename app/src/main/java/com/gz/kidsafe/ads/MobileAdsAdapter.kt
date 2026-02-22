package com.gz.kidsafe.ads

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

interface MobileAdsAdapter {
    fun setRequestConfiguration(configuration: RequestConfiguration)
    fun initialize(context: Context, onInitialized: () -> Unit)
}

object GoogleMobileAdsAdapter : MobileAdsAdapter {
    override fun setRequestConfiguration(configuration: RequestConfiguration) {
        MobileAds.setRequestConfiguration(configuration)
    }

    override fun initialize(context: Context, onInitialized: () -> Unit) {
        MobileAds.initialize(context) {
            onInitialized()
        }
    }
}
