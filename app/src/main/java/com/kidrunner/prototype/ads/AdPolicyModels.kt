package com.kidrunner.prototype.ads

data class AdRuntimeProfile(
    val appInFamiliesProgram: Boolean,
    val includesChildrenAudience: Boolean,
    val mixedAudience: Boolean,
    val userAgeKnown: Boolean,
    val isUnderAge: Boolean,
    val isEea: Boolean,
    val mediationNetworksFamiliesCertified: Boolean
)

data class AdFeatureFlags(
    val adsEnabled: Boolean,
    val rewardedContinueEnabled: Boolean,
    val childSafeMode: Boolean,
    val allowUnknownAgeMonetization: Boolean
)

data class PolicyDecision(
    val allowAds: Boolean,
    val childDirected: Boolean,
    val maxAdContentRating: String,
    val underAgeOfConsent: Boolean,
    val reasons: List<String>
)
