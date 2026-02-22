package com.kidrunner.prototype.ads

/**
 * Compliance-by-design policy gate for COPPA + Google Play Families.
 *
 * This class decides whether ad serving can proceed and what restrictions apply.
 */
class KidSafeAdPolicy {

    fun evaluate(profile: AdRuntimeProfile, flags: AdFeatureFlags): PolicyDecision {
        if (!flags.adsEnabled) {
            return PolicyDecision(
                allowAds = false,
                childDirected = true,
                maxAdContentRating = "G",
                underAgeOfConsent = profile.isEea && profile.isUnderAge,
                reasons = listOf("ads_disabled_feature_flag")
            )
        }

        if (flags.childSafeMode) {
            if (!profile.mediationNetworksFamiliesCertified) {
                return PolicyDecision(
                    allowAds = false,
                    childDirected = true,
                    maxAdContentRating = "G",
                    underAgeOfConsent = profile.isEea && profile.isUnderAge,
                    reasons = listOf("mediation_not_families_certified")
                )
            }

            val isChildTraffic = profile.includesChildrenAudience || !profile.userAgeKnown || profile.isUnderAge
            if (isChildTraffic) {
                if (!flags.allowUnknownAgeMonetization && !profile.userAgeKnown) {
                    return PolicyDecision(
                        allowAds = false,
                        childDirected = true,
                        maxAdContentRating = "G",
                        underAgeOfConsent = profile.isEea && profile.isUnderAge,
                        reasons = listOf("unknown_age_monetization_disabled")
                    )
                }

                return PolicyDecision(
                    allowAds = true,
                    childDirected = true,
                    maxAdContentRating = "G",
                    underAgeOfConsent = profile.isEea && profile.isUnderAge,
                    reasons = listOf("child_safe_mode_active")
                )
            }
        }

        return PolicyDecision(
            allowAds = true,
            childDirected = false,
            maxAdContentRating = "PG",
            underAgeOfConsent = profile.isEea && profile.isUnderAge,
            reasons = listOf("non_child_traffic")
        )
    }
}
