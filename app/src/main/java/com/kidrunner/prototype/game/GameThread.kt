package com.kidrunner.prototype.game

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder

class GameThread(
    private val holder: SurfaceHolder,
    private val world: GameWorld,
    private val onRender: (Canvas) -> Unit,
    private val onRunEnded: (RunEndEvent) -> Unit,
    private val onDebugStats: (fps: Int, frameMs: Float) -> Unit
) : Thread() {

    @Volatile
    private var running = false

    private val fixedStepSec = 1f / 30f
    private val fixedStepNs = (fixedStepSec * 1_000_000_000L).toLong()

    fun startLoop() {
        running = true
        start()
    }

    fun stopLoop() {
        running = false
        join(800)
    }

    override fun run() {
        var lastTime = System.nanoTime()
        var accumulator = 0L

        var frameCount = 0
        var fpsWindowStart = lastTime

        while (running) {
            val now = System.nanoTime()
            val frameNs = now - lastTime
            lastTime = now
            accumulator += frameNs

            var updates = 0
            while (accumulator >= fixedStepNs && updates < 5) {
                val ended = world.update(fixedStepSec)
                if (ended != null) onRunEnded(ended)
                accumulator -= fixedStepNs
                updates++
            }

            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    onRender(canvas)
                }
            } catch (t: Throwable) {
                Log.e("KidRunner", "Render loop error", t)
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }

            frameCount++
            val fpsElapsed = now - fpsWindowStart
            if (fpsElapsed >= 1_000_000_000L) {
                val fps = frameCount
                val frameMs = frameNs / 1_000_000f
                onDebugStats(fps, frameMs)
                frameCount = 0
                fpsWindowStart = now
            }
        }
    }
}
