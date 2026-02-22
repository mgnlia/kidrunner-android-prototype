package com.gz.kidsafe.ads

enum class AdBlockReason {
    ADS_DISABLED,
    AGE_UNKNOWN,
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
        policyDeclarationsFinalized: Boolean,
        policyFlagsValid: Boolean
    ): AdEligibilityDecision {
        if (!adsEnabled) {
            return AdEligibilityDecision(allowed = false, blockReason = AdBlockReason.ADS_DISABLED)
        }

        if (ageSignal == AgeSignal.UNKNOWN) {
            return AdEligibilityDecision(allowed = false, blockReason = AdBlockReason.AGE_UNKNOWN)
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
