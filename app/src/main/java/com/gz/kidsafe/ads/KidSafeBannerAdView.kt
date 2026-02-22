package com.gz.kidsafe.ads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.gz.kidsafe.BuildConfig

@Composable
fun KidSafeBannerAdView(
    modifier: Modifier = Modifier,
    ageSignal: AgeSignal = AgeSignal.UNKNOWN,
    policyDeclarationsFinalized: Boolean = BuildConfig.POLICY_DECLARATIONS_FINALIZED
) {
    val decision = AdEligibilityGuard.evaluate(
        ageSignal = ageSignal,
        adsEnabled = BuildConfig.KIDSAFE_ADS_ENABLED,
        policyDeclarationsFinalized = policyDeclarationsFinalized,
        policyFlagsValid = AdPolicyConfig.isStrictKidSafeConfigValid()
    )

    if (!decision.allowed) return

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
