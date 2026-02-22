package com.gz.kidsafe.ads

import android.content.Context
import android.util.Log
import com.gz.kidsafe.BuildConfig
import com.gz.kidsafe.analytics.MonetizationAnalyticsTracker
import com.gz.kidsafe.analytics.MonetizationEventType
import java.util.concurrent.atomic.AtomicBoolean

object KidSafeAdsManager {
    private const val TAG = "KidSafeAdsManager"
    private val initialized = AtomicBoolean(false)

    fun initialize(
        context: Context,
        ageSignal: AgeSignal = AgeSignal.UNKNOWN,
        adsEnabled: Boolean = BuildConfig.KIDSAFE_ADS_ENABLED,
        policyDeclarationsFinalized: Boolean = BuildConfig.POLICY_DECLARATIONS_FINALIZED,
        realAdapterEnabled: Boolean = BuildConfig.MOBILE_ADS_REAL_ADAPTER_ENABLED,
        mobileAdsAdapter: MobileAdsAdapter = MobileAdsAdapterResolver.resolve(realAdapterEnabled),
        policyFlagsValid: Boolean = AdPolicyConfig.isStrictKidSafeConfigValid()
    ): AdEligibilityDecision {
        MonetizationAnalyticsTracker.recordEvent(
            type = MonetizationEventType.ADS_INIT_ATTEMPTED,
            details = "ageSignal=$ageSignal"
        )

        val decision = AdEligibilityGuard.evaluate(
            ageSignal = ageSignal,
            adsEnabled = adsEnabled,
            mobileAdsRealAdapterEnabled = realAdapterEnabled,
            policyDeclarationsFinalized = policyDeclarationsFinalized,
            policyFlagsValid = policyFlagsValid
        )

        if (!decision.allowed) {
            decision.blockReason?.let {
                MonetizationAnalyticsTracker.recordEligibilityBlocked(
                    type = MonetizationEventType.ADS_INIT_BLOCKED,
                    reason = it,
                    details = "ageSignal=$ageSignal"
                )
            }
            Log.w(TAG, "Ads initialization blocked (reason=${decision.blockReason})")
            return decision
        }

        MonetizationAnalyticsTracker.recordEvent(
            type = MonetizationEventType.ADS_INIT_ALLOWED,
            details = "ageSignal=$ageSignal"
        )

        if (initialized.compareAndSet(false, true)) {
            mobileAdsAdapter.setRequestConfiguration(AdPolicyConfig.requestConfiguration)
            mobileAdsAdapter.initialize(context) {
                Log.i(TAG, "MobileAds initialized in kid-safe mode (realAdapterEnabled=$realAdapterEnabled)")
            }
        }

        return decision
    }

    internal fun resetForTests() {
        initialized.set(false)
    }
}
