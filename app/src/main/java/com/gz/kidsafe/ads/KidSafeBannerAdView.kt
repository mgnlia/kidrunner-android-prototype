package com.gz.kidsafe.ads

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.gz.kidsafe.BuildConfig
import com.gz.kidsafe.analytics.MonetizationAnalyticsTracker
import com.gz.kidsafe.analytics.MonetizationEventType

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
        decision.blockReason?.let {
            MonetizationAnalyticsTracker.recordEligibilityBlocked(
                type = MonetizationEventType.BANNER_ELIGIBILITY_BLOCKED,
                reason = it,
                details = "ageSignal=$ageSignal"
            )
        }
        Log.w(TAG, "Banner blocked by fail-closed guard (reason=${decision.blockReason})")
        return
    }

    MonetizationAnalyticsTracker.recordEvent(
        type = MonetizationEventType.BANNER_ELIGIBILITY_ALLOWED,
        details = "ageSignal=$ageSignal"
    )

    val context = LocalContext.current
    val adView = remember(context) {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = AdUnitIdResolver.bannerAdUnitId()
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.BANNER_LOADED)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    MonetizationAnalyticsTracker.recordEvent(
                        type = MonetizationEventType.BANNER_FAILED_TO_LOAD,
                        details = "code=${error.code}"
                    )
                }

                override fun onAdImpression() {
                    MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.BANNER_IMPRESSION)
                }

                override fun onAdClicked() {
                    MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.BANNER_CLICKED)
                }
            }

            MonetizationAnalyticsTracker.recordEvent(
                type = MonetizationEventType.BANNER_REQUESTED,
                details = "adUnitConfigured"
            )
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
