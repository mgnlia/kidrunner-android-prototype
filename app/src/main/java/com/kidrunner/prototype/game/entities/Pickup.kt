package com.kidrunner.prototype.game.entities

class Pickup {
    var active: Boolean = false
    var x: Float = 0f
    var y: Float = 0f
    var radius: Float = 14f
    var speed: Float = 180f

    fun reset(x: Float, y: Float, radius: Float, speed: Float) {
        this.active = true
        this.x = x
        this.y = y
        this.radius = radius
        this.speed = speed
    }
}
