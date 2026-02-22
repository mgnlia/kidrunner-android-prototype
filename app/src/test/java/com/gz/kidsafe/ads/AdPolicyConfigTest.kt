package com.gz.kidsafe.ads

import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.RequestConfiguration
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdPolicyConfigTest {

    @Test
    fun `request config enforces kid-safe flags`() {
        val config = AdPolicyConfig.requestConfiguration
        assertEquals(
            RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE,
            config.tagForChildDirectedTreatment
        )
        assertEquals(
            RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE,
            config.tagForUnderAgeOfConsent
        )
        assertEquals(RequestConfiguration.MAX_AD_CONTENT_RATING_G, config.maxAdContentRating)
    }

    @Test
    fun `strict kid-safe config validation passes`() {
        assertTrue(AdPolicyConfig.isStrictKidSafeConfigValid())
    }

    @Test
    fun `ad requests are non-personalized`() {
        val request = AdPolicyConfig.buildNonPersonalizedAdRequest()
        val extras = request.getNetworkExtrasBundle(AdMobAdapter::class.java)
        assertEquals("1", extras?.getString("npa"))
    }
}
