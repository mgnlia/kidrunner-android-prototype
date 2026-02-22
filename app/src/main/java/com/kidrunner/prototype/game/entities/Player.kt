package com.kidrunner.prototype.game.entities

class Player(
    var x: Float = 0f,
    var y: Float = 0f,
    var radius: Float = 28f,
    var targetX: Float = 0f,
    var moveSpeed: Float = 520f
) {
    fun update(deltaSec: Float, minX: Float, maxX: Float) {
        val dx = targetX - x
        val maxStep = moveSpeed * deltaSec
        x += when {
            kotlin.math.abs(dx) <= maxStep -> dx
            dx > 0f -> maxStep
            else -> -maxStep
        }
        x = x.coerceIn(minX, maxX)
    }
}
