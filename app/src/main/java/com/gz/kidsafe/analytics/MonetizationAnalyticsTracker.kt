package com.gz.kidsafe.analytics

import com.gz.kidsafe.ads.AdBlockReason
import java.util.ArrayDeque

enum class MonetizationEventType {
    ADS_INIT_ATTEMPTED,
    ADS_INIT_ALLOWED,
    ADS_INIT_BLOCKED,
    BANNER_ELIGIBILITY_ALLOWED,
    BANNER_ELIGIBILITY_BLOCKED,
    BANNER_REQUESTED,
    BANNER_LOADED,
    BANNER_FAILED_TO_LOAD,
    BANNER_IMPRESSION,
    BANNER_CLICKED,
    PARENT_GATE_PROMPTED,
    PARENT_GATE_PASSED,
    PARENT_GATE_FAILED
}

data class MonetizationEvent(
    val type: MonetizationEventType,
    val timestampMs: Long = System.currentTimeMillis(),
    val details: String? = null
)

data class MonetizationKpiSnapshot(
    val initAttempts: Int,
    val initAllowed: Int,
    val initBlocked: Int,
    val bannerEligibilityAllowed: Int,
    val bannerEligibilityBlocked: Int,
    val bannerRequests: Int,
    val bannerLoads: Int,
    val bannerLoadFailures: Int,
    val bannerImpressions: Int,
    val bannerClicks: Int,
    val parentGatePrompted: Int,
    val parentGatePassed: Int,
    val parentGateFailed: Int,
    val blockedByReason: Map<AdBlockReason, Int>,
    val recentEvents: List<MonetizationEvent>
) {
    val bannerLoadRate: Double
        get() = ratio(bannerLoads, bannerRequests)

    val bannerClickThroughRate: Double
        get() = ratio(bannerClicks, bannerImpressions)

    val bannerBlockRate: Double
        get() = ratio(bannerEligibilityBlocked, bannerEligibilityAllowed + bannerEligibilityBlocked)

    val parentGateSuccessRate: Double
        get() = ratio(parentGatePassed, parentGatePrompted)

    private fun ratio(numerator: Int, denominator: Int): Double {
        if (denominator == 0) return 0.0
        return numerator.toDouble() / denominator.toDouble()
    }
}

object MonetizationAnalyticsTracker {
    private const val MAX_RECENT_EVENTS = 100

    private val lock = Any()
    private val counters: MutableMap<MonetizationEventType, Int> = mutableMapOf()
    private val blockReasonCounters: MutableMap<AdBlockReason, Int> = mutableMapOf()
    private val recentEvents: ArrayDeque<MonetizationEvent> = ArrayDeque()

    fun recordEvent(type: MonetizationEventType, details: String? = null) {
        synchronized(lock) {
            incrementCounter(type)
            appendEvent(MonetizationEvent(type = type, details = details))
        }
    }

    fun recordEligibilityBlocked(type: MonetizationEventType, reason: AdBlockReason, details: String? = null) {
        require(type == MonetizationEventType.ADS_INIT_BLOCKED || type == MonetizationEventType.BANNER_ELIGIBILITY_BLOCKED) {
            "Blocked tracking only supports ADS_INIT_BLOCKED or BANNER_ELIGIBILITY_BLOCKED"
        }

        synchronized(lock) {
            incrementCounter(type)
            blockReasonCounters[reason] = (blockReasonCounters[reason] ?: 0) + 1
            appendEvent(
                MonetizationEvent(
                    type = type,
                    details = details ?: reason.name
                )
            )
        }
    }

    fun snapshot(): MonetizationKpiSnapshot {
        synchronized(lock) {
            return MonetizationKpiSnapshot(
                initAttempts = countOf(MonetizationEventType.ADS_INIT_ATTEMPTED),
                initAllowed = countOf(MonetizationEventType.ADS_INIT_ALLOWED),
                initBlocked = countOf(MonetizationEventType.ADS_INIT_BLOCKED),
                bannerEligibilityAllowed = countOf(MonetizationEventType.BANNER_ELIGIBILITY_ALLOWED),
                bannerEligibilityBlocked = countOf(MonetizationEventType.BANNER_ELIGIBILITY_BLOCKED),
                bannerRequests = countOf(MonetizationEventType.BANNER_REQUESTED),
                bannerLoads = countOf(MonetizationEventType.BANNER_LOADED),
                bannerLoadFailures = countOf(MonetizationEventType.BANNER_FAILED_TO_LOAD),
                bannerImpressions = countOf(MonetizationEventType.BANNER_IMPRESSION),
                bannerClicks = countOf(MonetizationEventType.BANNER_CLICKED),
                parentGatePrompted = countOf(MonetizationEventType.PARENT_GATE_PROMPTED),
                parentGatePassed = countOf(MonetizationEventType.PARENT_GATE_PASSED),
                parentGateFailed = countOf(MonetizationEventType.PARENT_GATE_FAILED),
                blockedByReason = blockReasonCounters.toMap(),
                recentEvents = recentEvents.toList()
            )
        }
    }

    internal fun resetForTests() {
        synchronized(lock) {
            counters.clear()
            blockReasonCounters.clear()
            recentEvents.clear()
        }
    }

    private fun incrementCounter(type: MonetizationEventType) {
        counters[type] = (counters[type] ?: 0) + 1
    }

    private fun countOf(type: MonetizationEventType): Int = counters[type] ?: 0

    private fun appendEvent(event: MonetizationEvent) {
        recentEvents.addLast(event)
        while (recentEvents.size > MAX_RECENT_EVENTS) {
            recentEvents.removeFirst()
        }
    }
}
