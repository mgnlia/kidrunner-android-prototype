package com.kidrunner.prototype.game

data class RunEndEvent(
    val durationSec: Float,
    val score: Int,
    val deathReason: String
)
