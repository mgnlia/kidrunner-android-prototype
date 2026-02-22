package com.gz.kidsafe.ads

import android.content.Context
import android.util.Log
import com.gz.kidsafe.BuildConfig
import java.util.concurrent.atomic.AtomicBoolean

object KidSafeAdsManager {
    private const val TAG = "KidSafeAdsManager"
    private val initialized = AtomicBoolean(false)

    fun initialize(
        context: Context,
        ageSignal: AgeSignal = AgeSignal.UNKNOWN,
        policyDeclarationsFinalized: Boolean = BuildConfig.POLICY_DECLARATIONS_FINALIZED,
        realAdapterEnabled: Boolean = BuildConfig.MOBILE_ADS_REAL_ADAPTER_ENABLED,
        mobileAdsAdapter: MobileAdsAdapter = MobileAdsAdapterResolver.resolve(realAdapterEnabled),
        policyFlagsValid: Boolean = AdPolicyConfig.isStrictKidSafeConfigValid()
    ): AdEligibilityDecision {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = ageSignal,
            adsEnabled = BuildConfig.KIDSAFE_ADS_ENABLED,
            mobileAdsRealAdapterEnabled = realAdapterEnabled,
            policyDeclarationsFinalized = policyDeclarationsFinalized,
            policyFlagsValid = policyFlagsValid
        )

        if (!decision.allowed) {
            Log.w(TAG, "Ads initialization blocked (reason=${decision.blockReason})")
            return decision
        }

        if (initialized.compareAndSet(false, true)) {
            mobileAdsAdapter.setRequestConfiguration(AdPolicyConfig.requestConfiguration)
            mobileAdsAdapter.initialize(context) {
                Log.i(TAG, "MobileAds initialized in kid-safe mode (realAdapterEnabled=$realAdapterEnabled)")
            }
        }

        return decision
    }
}
