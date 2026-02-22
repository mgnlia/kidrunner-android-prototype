package com.gz.kidsafe.ads

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class KillSwitchValidationTest {

    @Test
    fun killSwitchDisabledBlocksAds() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = false,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = true,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.ADS_DISABLED, decision.blockReason)
    }

    @Test
    fun unknownAgeFailsClosedEvenWhenAdsEnabled() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.UNKNOWN,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = true,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.AGE_UNKNOWN, decision.blockReason)
    }
}
