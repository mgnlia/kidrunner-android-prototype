package com.gz.kidsafe.ads

import com.gz.kidsafe.BuildConfig

private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

object AdUnitIdResolver {
    fun bannerAdUnitId(): String {
        return resolveBannerAdUnitId(
            prodIdsApproved = BuildConfig.ADMOB_PROD_IDS_APPROVED,
            configuredProdBannerId = BuildConfig.ADMOB_BANNER_AD_UNIT_ID
        )
    }

    internal fun resolveBannerAdUnitId(
        prodIdsApproved: Boolean,
        configuredProdBannerId: String?
    ): String {
        val sanitizedProdId = configuredProdBannerId?.trim().orEmpty()
        return if (prodIdsApproved && sanitizedProdId.isNotEmpty()) {
            sanitizedProdId
        } else {
            TEST_BANNER_AD_UNIT_ID
        }
    }
}
