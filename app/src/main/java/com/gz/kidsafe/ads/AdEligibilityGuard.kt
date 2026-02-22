package com.gz.kidsafe.ads

enum class AdBlockReason {
    ADS_DISABLED,
    AGE_UNKNOWN,
    MOBILE_ADS_ADAPTER_DISABLED,
    POLICY_DECLARATIONS_PENDING,
    POLICY_FLAGS_INVALID
}

data class AdEligibilityDecision(
    val allowed: Boolean,
    val blockReason: AdBlockReason? = null
)

object AdEligibilityGuard {
    fun evaluate(
        ageSignal: AgeSignal,
        adsEnabled: Boolean,
        mobileAdsRealAdapterEnabled: Boolean,
        policyDeclarationsFinalized: Boolean,
        policyFlagsValid: Boolean
    ): AdEligibilityDecision {
        if (!adsEnabled) {
            return AdEligibilityDecision(allowed = false, blockReason = AdBlockReason.ADS_DISABLED)
        }

        // Fail-closed default: unknown age means strict no-ads.
        if (ageSignal == AgeSignal.UNKNOWN) {
            return AdEligibilityDecision(allowed = false, blockReason = AdBlockReason.AGE_UNKNOWN)
        }

        // MobileAds wiring stays behind an explicit runtime/compile-time flag gate.
        if (!mobileAdsRealAdapterEnabled) {
            return AdEligibilityDecision(allowed = false, blockReason = AdBlockReason.MOBILE_ADS_ADAPTER_DISABLED)
        }

        if (!policyDeclarationsFinalized) {
            return AdEligibilityDecision(allowed = false, blockReason = AdBlockReason.POLICY_DECLARATIONS_PENDING)
        }

        if (!policyFlagsValid) {
            return AdEligibilityDecision(allowed = false, blockReason = AdBlockReason.POLICY_FLAGS_INVALID)
        }

        return AdEligibilityDecision(allowed = true)
    }
}
