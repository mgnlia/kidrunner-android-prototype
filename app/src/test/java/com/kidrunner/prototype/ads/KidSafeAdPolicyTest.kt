package com.kidrunner.prototype.ads

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KidSafeAdPolicyTest {

    private val policy = KidSafeAdPolicy()

    @Test
    fun `ads disabled flag forces no ad mode`() {
        val decision = policy.evaluate(
            profile = baseProfile(),
            flags = AdFeatureFlags(
                adsEnabled = false,
                rewardedContinueEnabled = false,
                childSafeMode = true,
                allowUnknownAgeMonetization = false
            )
        )

        assertFalse(decision.allowAds)
        assertTrue(decision.childDirected)
        assertEquals("G", decision.maxAdContentRating)
    }

    @Test
    fun `unknown age blocked when unknown-age monetization is disabled`() {
        val decision = policy.evaluate(
            profile = baseProfile(userAgeKnown = false),
            flags = AdFeatureFlags(
                adsEnabled = true,
                rewardedContinueEnabled = false,
                childSafeMode = true,
                allowUnknownAgeMonetization = false
            )
        )

        assertFalse(decision.allowAds)
        assertTrue(decision.reasons.contains("unknown_age_monetization_disabled"))
    }

    @Test
    fun `child-safe mode allows only when families certified and compliant`() {
        val decision = policy.evaluate(
            profile = baseProfile(userAgeKnown = true, isUnderAge = true, mediationCertified = true),
            flags = AdFeatureFlags(
                adsEnabled = true,
                rewardedContinueEnabled = false,
                childSafeMode = true,
                allowUnknownAgeMonetization = false
            )
        )

        assertTrue(decision.allowAds)
        assertTrue(decision.childDirected)
        assertEquals("G", decision.maxAdContentRating)
    }

    @Test
    fun `blocks when mediation not families-certified`() {
        val decision = policy.evaluate(
            profile = baseProfile(userAgeKnown = true, isUnderAge = true, mediationCertified = false),
            flags = AdFeatureFlags(
                adsEnabled = true,
                rewardedContinueEnabled = false,
                childSafeMode = true,
                allowUnknownAgeMonetization = true
            )
        )

        assertFalse(decision.allowAds)
        assertTrue(decision.reasons.contains("mediation_not_families_certified"))
    }

    private fun baseProfile(
        userAgeKnown: Boolean = false,
        isUnderAge: Boolean = true,
        mediationCertified: Boolean = true
    ): AdRuntimeProfile {
        return AdRuntimeProfile(
            appInFamiliesProgram = true,
            includesChildrenAudience = true,
            mixedAudience = false,
            userAgeKnown = userAgeKnown,
            isUnderAge = isUnderAge,
            isEea = false,
            mediationNetworksFamiliesCertified = mediationCertified
        )
    }
}
