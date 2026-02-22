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

object NoOpMobileAdsAdapter : MobileAdsAdapter {
    override fun setRequestConfiguration(configuration: RequestConfiguration) = Unit

    override fun initialize(context: Context, onInitialized: () -> Unit) {
        onInitialized()
    }
}

object MobileAdsAdapterResolver {
    fun resolve(realAdapterEnabled: Boolean): MobileAdsAdapter {
        return if (realAdapterEnabled) GoogleMobileAdsAdapter else NoOpMobileAdsAdapter
    }
}
