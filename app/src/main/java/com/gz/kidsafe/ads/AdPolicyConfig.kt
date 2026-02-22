package com.gz.kidsafe.ads

import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.RequestConfiguration

/**
 * Single source of truth for kid-safe ad policy.
 */
object AdPolicyConfig {
    const val TAG_FOR_CHILD_DIRECTED_TREATMENT = RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
    const val TAG_FOR_UNDER_AGE_OF_CONSENT = RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE
    const val MAX_AD_CONTENT_RATING = RequestConfiguration.MAX_AD_CONTENT_RATING_G

    const val NON_PERSONALIZED_EXTRAS_KEY = "npa"
    const val NON_PERSONALIZED_EXTRAS_VALUE = "1"

    val requestConfiguration: RequestConfiguration =
        RequestConfiguration.Builder()
            .setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT)
            .setTagForUnderAgeOfConsent(TAG_FOR_UNDER_AGE_OF_CONSENT)
            .setMaxAdContentRating(MAX_AD_CONTENT_RATING)
            .build()

    fun isStrictKidSafeConfigValid(): Boolean {
        return TAG_FOR_CHILD_DIRECTED_TREATMENT == RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE &&
            TAG_FOR_UNDER_AGE_OF_CONSENT == RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE &&
            MAX_AD_CONTENT_RATING == RequestConfiguration.MAX_AD_CONTENT_RATING_G
    }

    /**
     * JVM-safe representation used by unit tests; Android Bundle conversion happens at request-build time.
     */
    fun nonPersonalizedRequestExtras(): Map<String, String> =
        mapOf(NON_PERSONALIZED_EXTRAS_KEY to NON_PERSONALIZED_EXTRAS_VALUE)

    fun buildNonPersonalizedAdRequest(): AdRequest {
        val extras = Bundle().apply {
            nonPersonalizedRequestExtras().forEach { (key, value) -> putString(key, value) }
        }

        return AdRequest.Builder()
            .addNetworkExtrasBundle(com.google.ads.mediation.admob.AdMobAdapter::class.java, extras)
            .build()
    }
}
