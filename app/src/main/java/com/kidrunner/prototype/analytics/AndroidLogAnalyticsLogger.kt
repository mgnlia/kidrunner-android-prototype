package com.kidrunner.prototype.analytics

import android.util.Log

class AndroidLogAnalyticsLogger : AnalyticsLogger {
    override fun log(eventName: String, params: Map<String, Any>) {
        Log.d("KidRunnerAnalytics", "event=$eventName params=$params")
    }
}
