package com.gz.kidsafe.ads

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdEligibilityPriorityMatrixTest {

    @Test
    fun `ads disabled wins regardless of all other inputs`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.ADULT_18_PLUS,
            adsEnabled = false,
            mobileAdsRealAdapterEnabled = false,
            policyDeclarationsFinalized = false,
            policyFlagsValid = false
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.ADS_DISABLED, decision.blockReason)
    }

    @Test
    fun `unknown age blocks before adapter and policy checks`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.UNKNOWN,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = false,
            policyDeclarationsFinalized = false,
            policyFlagsValid = false
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.AGE_UNKNOWN, decision.blockReason)
    }

    @Test
    fun `adapter disabled blocks before policy declaration and policy flags`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = false,
            policyDeclarationsFinalized = false,
            policyFlagsValid = false
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.MOBILE_ADS_ADAPTER_DISABLED, decision.blockReason)
    }

    @Test
    fun `policy declarations pending blocks before policy flag validation`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = false,
            policyFlagsValid = false
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.POLICY_DECLARATIONS_PENDING, decision.blockReason)
    }

    @Test
    fun `all gates open allows ad eligibility`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = true,
            policyFlagsValid = true
        )

        assertTrue(decision.allowed)
        assertEquals(null, decision.blockReason)
    }
}
