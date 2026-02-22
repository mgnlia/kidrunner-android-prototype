package com.kidrunner.prototype.game.entities

class Obstacle {
    var active: Boolean = false
    var x: Float = 0f
    var y: Float = 0f
    var radius: Float = 20f
    var speed: Float = 200f

    fun reset(x: Float, y: Float, radius: Float, speed: Float) {
        this.active = true
        this.x = x
        this.y = y
        this.radius = radius
        this.speed = speed
    }
}
