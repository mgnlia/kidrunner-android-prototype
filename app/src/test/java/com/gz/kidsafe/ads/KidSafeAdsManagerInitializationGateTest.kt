package com.gz.kidsafe.ads

import android.content.Context
import android.content.ContextWrapper
import com.google.android.gms.ads.RequestConfiguration
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KidSafeAdsManagerInitializationGateTest {

    @Before
    fun setup() {
        KidSafeAdsManager.resetForTests()
    }

    @Test
    fun `blocked initialization does not touch mobile ads adapter`() {
        val adapter = FakeMobileAdsAdapter()

        val decision = KidSafeAdsManager.initialize(
            context = testContext(),
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = false,
            policyDeclarationsFinalized = true,
            realAdapterEnabled = true,
            mobileAdsAdapter = adapter,
            policyFlagsValid = true
        )

        assertFalse(decision.allowed)
        assertEquals(AdBlockReason.ADS_DISABLED, decision.blockReason)
        assertEquals(0, adapter.setRequestConfigurationCount)
        assertEquals(0, adapter.initializeCount)
    }

    @Test
    fun `allowed initialization applies config and initializes adapter once`() {
        val adapter = FakeMobileAdsAdapter()

        val decision = KidSafeAdsManager.initialize(
            context = testContext(),
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = true,
            policyDeclarationsFinalized = true,
            realAdapterEnabled = true,
            mobileAdsAdapter = adapter,
            policyFlagsValid = true
        )

        assertTrue(decision.allowed)
        assertEquals(1, adapter.setRequestConfigurationCount)
        assertEquals(1, adapter.initializeCount)
        assertNotNull(adapter.lastConfiguration)
    }

    @Test
    fun `allowed initialization is idempotent across repeated calls`() {
        val adapter = FakeMobileAdsAdapter()
        val context = testContext()

        val first = KidSafeAdsManager.initialize(
            context = context,
            ageSignal = AgeSignal.CHILD_U13,
            adsEnabled = true,
            policyDeclarationsFinalized = true,
            realAdapterEnabled = true,
            mobileAdsAdapter = adapter,
            policyFlagsValid = true
        )

        val second = KidSafeAdsManager.initialize(
            context = context,
            ageSignal = AgeSignal.ADULT_18_PLUS,
            adsEnabled = true,
            policyDeclarationsFinalized = true,
            realAdapterEnabled = true,
            mobileAdsAdapter = adapter,
            policyFlagsValid = true
        )

        assertTrue(first.allowed)
        assertTrue(second.allowed)
        assertEquals(1, adapter.setRequestConfigurationCount)
        assertEquals(1, adapter.initializeCount)
    }

    private fun testContext(): Context = ContextWrapper(null)

    private class FakeMobileAdsAdapter : MobileAdsAdapter {
        var setRequestConfigurationCount: Int = 0
        var initializeCount: Int = 0
        var lastConfiguration: RequestConfiguration? = null

        override fun setRequestConfiguration(configuration: RequestConfiguration) {
            setRequestConfigurationCount += 1
            lastConfiguration = configuration
        }

        override fun initialize(context: Context, onInitialized: () -> Unit) {
            initializeCount += 1
            onInitialized()
        }
    }
}
