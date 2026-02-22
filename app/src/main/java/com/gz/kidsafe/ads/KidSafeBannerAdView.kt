package com.gz.kidsafe.ads

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.gz.kidsafe.BuildConfig

private const val TAG = "KidSafeBannerAdView"

@Composable
fun KidSafeBannerAdView(
    modifier: Modifier = Modifier,
    ageSignal: AgeSignal = AgeSignal.UNKNOWN,
    policyDeclarationsFinalized: Boolean = BuildConfig.POLICY_DECLARATIONS_FINALIZED,
    realAdapterEnabled: Boolean = BuildConfig.MOBILE_ADS_REAL_ADAPTER_ENABLED
) {
    val decision = AdEligibilityGuard.evaluate(
        ageSignal = ageSignal,
        adsEnabled = BuildConfig.KIDSAFE_ADS_ENABLED,
        mobileAdsRealAdapterEnabled = realAdapterEnabled,
        policyDeclarationsFinalized = policyDeclarationsFinalized,
        policyFlagsValid = AdPolicyConfig.isStrictKidSafeConfigValid()
    )

    if (!decision.allowed) {
        Log.w(TAG, "Banner blocked by fail-closed guard (reason=${decision.blockReason})")
        return
    }

    val context = LocalContext.current
    val adView = remember(context) {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = AdUnitIdResolver.bannerAdUnitId()
            loadAd(AdPolicyConfig.buildNonPersonalizedAdRequest())
        }
    }

    DisposableEffect(adView) {
        onDispose {
            adView.destroy()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { adView }
    )
}
