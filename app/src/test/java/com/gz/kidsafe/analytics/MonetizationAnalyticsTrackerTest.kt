package com.gz.kidsafe.analytics

import com.gz.kidsafe.ads.AdBlockReason
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MonetizationAnalyticsTrackerTest {

    @Before
    fun setup() {
        MonetizationAnalyticsTracker.resetForTests()
    }

    @Test
    fun `snapshot returns zeroed counters by default`() {
        val snapshot = MonetizationAnalyticsTracker.snapshot()

        assertEquals(0, snapshot.initAttempts)
        assertEquals(0, snapshot.initBlocked)
        assertEquals(0, snapshot.bannerRequests)
        assertEquals(0, snapshot.bannerLoads)
        assertEquals(0, snapshot.bannerLoadFailures)
        assertEquals(0.0, snapshot.bannerLoadRate, 0.0001)
        assertEquals(0.0, snapshot.bannerClickThroughRate, 0.0001)
        assertEquals(0.0, snapshot.bannerBlockRate, 0.0001)
        assertEquals(0.0, snapshot.parentGateSuccessRate, 0.0001)
        assertTrue(snapshot.blockedByReason.isEmpty())
        assertTrue(snapshot.recentEvents.isEmpty())
    }

    @Test
    fun `records blocked eligibility reasons and computes rates`() {
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.ADS_INIT_ATTEMPTED)
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.ADS_INIT_ALLOWED)
        MonetizationAnalyticsTracker.recordEligibilityBlocked(
            type = MonetizationEventType.BANNER_ELIGIBILITY_BLOCKED,
            reason = AdBlockReason.AGE_UNKNOWN
        )
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.BANNER_ELIGIBILITY_ALLOWED)
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.BANNER_REQUESTED)
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.BANNER_REQUESTED)
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.BANNER_LOADED)
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.BANNER_IMPRESSION)
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.BANNER_CLICKED)
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.PARENT_GATE_PROMPTED)
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.PARENT_GATE_PROMPTED)
        MonetizationAnalyticsTracker.recordEvent(MonetizationEventType.PARENT_GATE_PASSED)

        val snapshot = MonetizationAnalyticsTracker.snapshot()

        assertEquals(1, snapshot.initAttempts)
        assertEquals(1, snapshot.initAllowed)
        assertEquals(1, snapshot.bannerEligibilityAllowed)
        assertEquals(1, snapshot.bannerEligibilityBlocked)
        assertEquals(2, snapshot.bannerRequests)
        assertEquals(1, snapshot.bannerLoads)
        assertEquals(1, snapshot.bannerImpressions)
        assertEquals(1, snapshot.bannerClicks)
        assertEquals(2, snapshot.parentGatePrompted)
        assertEquals(1, snapshot.parentGatePassed)
        assertEquals(1, snapshot.blockedByReason[AdBlockReason.AGE_UNKNOWN])

        assertEquals(0.5, snapshot.bannerLoadRate, 0.0001)
        assertEquals(1.0, snapshot.bannerClickThroughRate, 0.0001)
        assertEquals(0.5, snapshot.bannerBlockRate, 0.0001)
        assertEquals(0.5, snapshot.parentGateSuccessRate, 0.0001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `recordEligibilityBlocked rejects unsupported event types`() {
        MonetizationAnalyticsTracker.recordEligibilityBlocked(
            type = MonetizationEventType.BANNER_REQUESTED,
            reason = AdBlockReason.AGE_UNKNOWN
        )
    }

    @Test
    fun `recent event list is capped at one hundred items`() {
        repeat(150) { index ->
            MonetizationAnalyticsTracker.recordEvent(
                type = MonetizationEventType.BANNER_REQUESTED,
                details = "event-$index"
            )
        }

        val snapshot = MonetizationAnalyticsTracker.snapshot()

        assertEquals(150, snapshot.bannerRequests)
        assertEquals(100, snapshot.recentEvents.size)
        assertEquals("event-50", snapshot.recentEvents.first().details)
        assertEquals("event-149", snapshot.recentEvents.last().details)
    }
}
