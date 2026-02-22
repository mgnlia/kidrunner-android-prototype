package com.gz.kidsafe.ads

import org.junit.Assert.assertEquals
import org.junit.Test

class AdUnitIdResolverTest {
    private val testId = "ca-app-pub-3940256099942544/6300978111"

    @Test
    fun `returns test ad unit when production ids are not approved`() {
        val result = AdUnitIdResolver.resolveBannerAdUnitId(
            prodIdsApproved = false,
            configuredProdBannerId = "ca-app-pub-xxx/yyy"
        )

        assertEquals(testId, result)
    }

    @Test
    fun `returns test ad unit when production id is blank`() {
        val result = AdUnitIdResolver.resolveBannerAdUnitId(
            prodIdsApproved = true,
            configuredProdBannerId = "  "
        )

        assertEquals(testId, result)
    }

    @Test
    fun `returns configured production ad unit only when approved and non-blank`() {
        val prodId = "ca-app-pub-1111111111111111/2222222222"
        val result = AdUnitIdResolver.resolveBannerAdUnitId(
            prodIdsApproved = true,
            configuredProdBannerId = prodId
        )

        assertEquals(prodId, result)
    }
}
