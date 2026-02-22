package com.kidrunner.prototype.ads

import com.kidrunner.prototype.analytics.AnalyticsLogger

class KidSafeAdController(
    private val policy: KidSafeAdPolicy,
    private val adapter: AdSdkAdapter,
    private val analytics: AnalyticsLogger,
    private val featureFlags: AdFeatureFlags
) {
    private var initialized = false
    private var decision: PolicyDecision? = null

    fun initialize(profile: AdRuntimeProfile) {
        decision = policy.evaluate(profile, featureFlags)
        val local = decision!!

        analytics.log(
            "ad_policy_decision",
            mapOf(
                "allow_ads" to local.allowAds,
                "child_directed" to local.childDirected,
                "max_rating" to local.maxAdContentRating,
                "under_age_of_consent" to local.underAgeOfConsent,
                "reasons" to local.reasons.joinToString("|")
            )
        )

        if (!local.allowAds) {
            analytics.log("ad_mode_disabled", mapOf("reason" to local.reasons.joinToString("|")))
            initialized = true
            return
        }

        adapter.initialize()
        adapter.applyRequestConfig(
            childDirected = local.childDirected,
            maxRating = local.maxAdContentRating,
            underAgeOfConsent = local.underAgeOfConsent
        )
        adapter.loadInterstitial()
        initialized = true
    }

    fun maybeShowInterstitial(onSkipped: (() -> Unit)? = null): Boolean {
        if (!initialized) return false
        val local = decision ?: return false
        if (!local.allowAds) {
            onSkipped?.invoke()
            return false
        }

        val shown = adapter.showInterstitialIfReady()
        if (!shown) onSkipped?.invoke()
        analytics.log("ad_interstitial_attempt", mapOf("shown" to shown))
        if (shown) adapter.loadInterstitial()
        return shown
    }

    fun canOfferRewardedContinue(): Boolean {
        val local = decision ?: return false
        if (!featureFlags.rewardedContinueEnabled) return false
        if (!local.allowAds) return false
        // Further gating if needed (session frequency cap, etc.).
        return true
    }
}
