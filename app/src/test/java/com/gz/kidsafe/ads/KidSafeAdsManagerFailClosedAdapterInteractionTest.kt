package com.gz.kidsafe.ads

import android.content.Context
import android.content.ContextWrapper
import com.google.android.gms.ads.RequestConfiguration
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class KidSafeAdsManagerFailClosedAdapterInteractionTest {

    private class FakeMobileAdsAdapter : MobileAdsAdapter {
        var initializeCalls: Int = 0
        var setRequestConfigurationCalls: Int = 0

        override fun setRequestConfiguration(configuration: RequestConfiguration) {
            setRequestConfigurationCalls += 1
        }

        override fun initialize(context: Context, onInitialized: () -> Unit) {
            initializeCalls += 1
            onInitialized()
        }
    }

    private fun blockedTestContext(): Context = ContextWrapper(null)

    @Test
    fun initialize_doesNotCallAdapterInitialize_whenAgeSignalUnknown() {
        val fakeAdapter = FakeMobileAdsAdapter()

        val decision = KidSafeAdsManager.initialize(
            context = blockedTestContext(),
            ageSignal = AgeSignal.UNKNOWN,
            policyDeclarationsFinalized = true,
            realAdapterEnabled = true,
            mobileAdsAdapter = fakeAdapter,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.AGE_UNKNOWN, decision.blockReason)
        assertEquals(0, fakeAdapter.initializeCalls)
    }

    @Test
    fun initialize_doesNotCallSetRequestConfiguration_whenAgeSignalUnknown() {
        val fakeAdapter = FakeMobileAdsAdapter()

        val decision = KidSafeAdsManager.initialize(
            context = blockedTestContext(),
            ageSignal = AgeSignal.UNKNOWN,
            policyDeclarationsFinalized = true,
            realAdapterEnabled = true,
            mobileAdsAdapter = fakeAdapter,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.AGE_UNKNOWN, decision.blockReason)
        assertEquals(0, fakeAdapter.setRequestConfigurationCalls)
    }

    @Test
    fun initialize_doesNotCallAdapterMethods_whenPolicyDeclarationsPending() {
        val fakeAdapter = FakeMobileAdsAdapter()

        val decision = KidSafeAdsManager.initialize(
            context = blockedTestContext(),
            ageSignal = AgeSignal.CHILD_U13,
            policyDeclarationsFinalized = false,
            realAdapterEnabled = true,
            mobileAdsAdapter = fakeAdapter,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.POLICY_DECLARATIONS_PENDING, decision.blockReason)
        assertEquals(0, fakeAdapter.initializeCalls)
        assertEquals(0, fakeAdapter.setRequestConfigurationCalls)
    }

    @Test
    fun initialize_doesNotCallAdapterMethods_whenPolicyFlagsInvalid() {
        val fakeAdapter = FakeMobileAdsAdapter()

        val decision = KidSafeAdsManager.initialize(
            context = blockedTestContext(),
            ageSignal = AgeSignal.CHILD_U13,
            policyDeclarationsFinalized = true,
            realAdapterEnabled = true,
            mobileAdsAdapter = fakeAdapter,
            policyFlagsValid = false
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.POLICY_FLAGS_INVALID, decision.blockReason)
        assertEquals(0, fakeAdapter.initializeCalls)
        assertEquals(0, fakeAdapter.setRequestConfigurationCalls)
    }

    @Test
    fun initialize_doesNotCallAdapterMethods_whenKillSwitchDisabled() {
        val fakeAdapter = FakeMobileAdsAdapter()

        val decision = KidSafeAdsManager.initialize(
            context = blockedTestContext(),
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = false,
            policyDeclarationsFinalized = true,
            realAdapterEnabled = true,
            mobileAdsAdapter = fakeAdapter,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.ADS_DISABLED, decision.blockReason)
        assertEquals(0, fakeAdapter.initializeCalls)
        assertEquals(0, fakeAdapter.setRequestConfigurationCalls)
    }
}
