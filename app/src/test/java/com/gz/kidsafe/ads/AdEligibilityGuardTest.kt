package com.gz.kidsafe.ads

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdEligibilityGuardTest {

    @Test
    fun `unknown age is blocked by default`() {
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

    @Test
    fun `unknown age remains blocked even when declarations are pending`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.UNKNOWN,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = false,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.AGE_UNKNOWN, decision.blockReason)
    }

    @Test
    fun `unknown age remains blocked even when policy flags are invalid`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.UNKNOWN,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = true,
            policyFlagsValid = false
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.AGE_UNKNOWN, decision.blockReason)
    }

    @Test
    fun `unknown age remains blocked when declarations are pending and policy flags are invalid`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.UNKNOWN,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = false,
            policyFlagsValid = false
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.AGE_UNKNOWN, decision.blockReason)
    }

    @Test
    fun `ads disabled blocks before unknown age check`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.UNKNOWN,
            adsEnabled = false,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = true,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.ADS_DISABLED, decision.blockReason)
    }

    @Test
    fun `ads disabled blocks before any other checks`() {
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
    fun `mobile ads adapter disabled blocks ad serving`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = false,
            policyDeclarationsFinalized = true,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.MOBILE_ADS_ADAPTER_DISABLED, decision.blockReason)
    }

    @Test
    fun `policy declarations pending blocks ad serving`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = false,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.POLICY_DECLARATIONS_PENDING, decision.blockReason)
    }

    @Test
    fun `invalid policy flags block ad serving`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = true,
            policyFlagsValid = false
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.POLICY_FLAGS_INVALID, decision.blockReason)
    }

    @Test
    fun `eligible child age passes when all checks are satisfied`() {
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

    @Test
    fun `eligible teen age passes when all checks are satisfied`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.TEEN_13_17,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = true,
            policyFlagsValid = true
        )

        assertTrue(decision.allowed)
        assertEquals(null, decision.blockReason)
    }

    @Test
    fun `eligible adult age passes when all checks are satisfied`() {
        val decision = AdEligibilityGuard.evaluate(
            ageSignal = AgeSignal.ADULT_18_PLUS,
            adsEnabled = true,
            mobileAdsRealAdapterEnabled = true,
            policyDeclarationsFinalized = true,
            policyFlagsValid = true
        )

        assertTrue(decision.allowed)
        assertEquals(null, decision.blockReason)
    }
}
