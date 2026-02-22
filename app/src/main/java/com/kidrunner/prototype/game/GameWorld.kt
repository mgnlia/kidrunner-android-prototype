package com.kidrunner.prototype.game

import com.kidrunner.prototype.game.entities.Obstacle
import com.kidrunner.prototype.game.entities.Pickup
import com.kidrunner.prototype.game.entities.Player
import kotlin.random.Random

class GameWorld {
    var width: Int = 1
    var height: Int = 1

    val player = Player()

    private val obstaclePool = Array(24) { Obstacle() }
    private val pickupPool = Array(16) { Pickup() }

    var score: Int = 0
        private set
    var bestScore: Int = 0
    var state: GameState = GameState.MENU
        private set

    private var elapsedSec: Float = 0f
    private var obstacleSpawnTimer: Float = 0f
    private var pickupSpawnTimer: Float = 0f
    private var surviveScoreAccumulator: Float = 0f

    fun onResize(w: Int, h: Int) {
        width = w.coerceAtLeast(1)
        height = h.coerceAtLeast(1)
        if (player.y <= 0f) {
            player.y = height * 0.82f
            player.x = width * 0.5f
            player.targetX = player.x
        }
    }

    fun startRun() {
        state = GameState.PLAYING
        score = 0
        elapsedSec = 0f
        obstacleSpawnTimer = 0f
        pickupSpawnTimer = 0f
        surviveScoreAccumulator = 0f

        player.x = width * 0.5f
        player.y = height * 0.82f
        player.targetX = player.x

        obstaclePool.forEach { it.active = false }
        pickupPool.forEach { it.active = false }
    }

    fun endRun() {
        state = GameState.GAME_OVER
        if (score > bestScore) bestScore = score
    }

    fun pauseRun() {
        if (state == GameState.PLAYING) state = GameState.PAUSED
    }

    fun resumeRun() {
        if (state == GameState.PAUSED) state = GameState.PLAYING
    }

    fun setPlayerTargetX(x: Float) {
        player.targetX = x
    }

    fun update(deltaSec: Float): RunEndEvent? {
        if (state != GameState.PLAYING) return null

        elapsedSec += deltaSec

        val minX = player.radius
        val maxX = width - player.radius
        player.update(deltaSec, minX, maxX)

        val difficulty = 1f + (elapsedSec / 15f).coerceAtMost(6f) * 0.2f

        obstacleSpawnTimer -= deltaSec
        if (obstacleSpawnTimer <= 0f) {
            spawnObstacle(difficulty)
            obstacleSpawnTimer = (0.8f / difficulty).coerceAtLeast(0.28f)
        }

        pickupSpawnTimer -= deltaSec
        if (pickupSpawnTimer <= 0f) {
            spawnPickup(difficulty)
            pickupSpawnTimer = (1.15f / difficulty).coerceAtLeast(0.5f)
        }

        for (o in obstaclePool) {
            if (!o.active) continue
            o.y += o.speed * deltaSec
            if (o.y - o.radius > height) {
                o.active = false
                continue
            }
            if (circlesOverlap(player.x, player.y, player.radius, o.x, o.y, o.radius)) {
                val event = RunEndEvent(
                    durationSec = elapsedSec,
                    score = score,
                    deathReason = "collision_obstacle"
                )
                endRun()
                return event
            }
        }

        for (p in pickupPool) {
            if (!p.active) continue
            p.y += p.speed * deltaSec
            if (p.y - p.radius > height) {
                p.active = false
                continue
            }
            if (circlesOverlap(player.x, player.y, player.radius, p.x, p.y, p.radius)) {
                p.active = false
                score += 10
            }
        }

        // survive score: +4 points per second
        surviveScoreAccumulator += deltaSec * 4f
        while (surviveScoreAccumulator >= 1f) {
            surviveScoreAccumulator -= 1f
            score += 1
        }

        return null
    }

    fun obstacles(): Array<Obstacle> = obstaclePool
    fun pickups(): Array<Pickup> = pickupPool

    private fun spawnObstacle(difficulty: Float) {
        val slot = obstaclePool.firstOrNull { !it.active } ?: return
        val x = Random.nextFloat() * (width - 40f) + 20f
        val radius = Random.nextFloat() * 14f + 16f
        val speed = (210f + Random.nextFloat() * 120f) * difficulty
        slot.reset(x, -radius, radius, speed)
    }

    private fun spawnPickup(difficulty: Float) {
        val slot = pickupPool.firstOrNull { !it.active } ?: return
        val x = Random.nextFloat() * (width - 40f) + 20f
        val radius = Random.nextFloat() * 8f + 12f
        val speed = (160f + Random.nextFloat() * 90f) * (0.95f + difficulty * 0.08f)
        slot.reset(x, -radius, radius, speed)
    }

    private fun circlesOverlap(x1: Float, y1: Float, r1: Float, x2: Float, y2: Float, r2: Float): Boolean {
        val dx = x2 - x1
        val dy = y2 - y1
        val rr = r1 + r2
        return dx * dx + dy * dy <= rr * rr
    }
}
