package com.kidrunner.prototype.ads

interface AdSdkAdapter {
    fun initialize()

    /**
     * Apply request configuration that maps to SDK-level child safety settings:
     * - tagForChildDirectedTreatment
     * - maxAdContentRating
     * - tagForUnderAgeOfConsent
     */
    fun applyRequestConfig(childDirected: Boolean, maxRating: String, underAgeOfConsent: Boolean)

    fun loadInterstitial()
    fun showInterstitialIfReady(): Boolean
}

class NoopAdSdkAdapter : AdSdkAdapter {
    override fun initialize() = Unit
    override fun applyRequestConfig(childDirected: Boolean, maxRating: String, underAgeOfConsent: Boolean) = Unit
    override fun loadInterstitial() = Unit
    override fun showInterstitialIfReady(): Boolean = false
}
