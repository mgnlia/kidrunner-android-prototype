package com.gz.kidsafe.ads

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class KidSafeAdsKillSwitchTest {

    @Test
    fun `kill switch blocks ads even when all other gates are open`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.ADULT_18_PLUS,
            adsEnabled = false,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = true,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.ADS_DISABLED, decision.blockReason)
    }

    @Test
    fun `kill switch blocks ads for child age signal`() {
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
}
