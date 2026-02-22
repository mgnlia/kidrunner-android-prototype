package com.kidrunner.prototype.analytics

interface AnalyticsLogger {
    fun log(eventName: String, params: Map<String, Any> = emptyMap())
}
