package com.kidrunner.prototype.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.kidrunner.prototype.analytics.AnalyticsLogger
import kotlin.math.roundToInt

class GameView(
    context: Context,
    private val analytics: AnalyticsLogger,
    private val onRunEnded: (RunEndEvent) -> Unit = {}
) : SurfaceView(context), SurfaceHolder.Callback {

    private val world = GameWorld()
    private var gameThread: GameThread? = null

    private val bgPaint = Paint().apply { color = Color.parseColor("#101A2E") }
    private val playerPaint = Paint().apply { color = Color.parseColor("#66D9EF") }
    private val obstaclePaint = Paint().apply { color = Color.parseColor("#FF6B6B") }
    private val pickupPaint = Paint().apply { color = Color.parseColor("#FFD93D") }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 42f
    }
    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#A5D6A7")
        textSize = 28f
    }

    @Volatile
    private var debugStats = DebugStats()

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        world.onResize(width, height)
        if (world.state == GameState.MENU) {
            analytics.log("session_start")
        }
        gameThread = GameThread(
            holder = holder,
            world = world,
            onRender = this::render,
            onRunEnded = this::handleRunEnded,
            onDebugStats = this::updateDebugStats
        ).also { it.startLoop() }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        world.onResize(width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameThread?.stopLoop()
        gameThread = null
    }

    fun resume() {
        if (holder.surface.isValid && gameThread == null) {
            gameThread = GameThread(
                holder = holder,
                world = world,
                onRender = this::render,
                onRunEnded = this::handleRunEnded,
                onDebugStats = this::updateDebugStats
            ).also { it.startLoop() }
        }
        if (world.state == GameState.PAUSED) {
            world.resumeRun()
            analytics.log("pause_resume")
        }
    }

    fun pause() {
        if (world.state == GameState.PLAYING) {
            world.pauseRun()
            analytics.log("pause_open")
        }
        gameThread?.stopLoop()
        gameThread = null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                when (world.state) {
                    GameState.MENU -> {
                        world.startRun()
                        analytics.log("menu_play_tap")
                        analytics.log("run_start")
                    }

                    GameState.GAME_OVER -> {
                        world.startRun()
                        analytics.log("retry_tap")
                        analytics.log("run_start")
                    }

                    GameState.PLAYING,
                    GameState.PAUSED -> world.setPlayerTargetX(event.x)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (world.state == GameState.PLAYING) {
                    world.setPlayerTargetX(event.x)
                }
            }
        }
        return true
    }

    private fun render(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        val p = world.player
        canvas.drawCircle(p.x, p.y, p.radius, playerPaint)

        world.obstacles().forEach {
            if (it.active) canvas.drawCircle(it.x, it.y, it.radius, obstaclePaint)
        }

        world.pickups().forEach {
            if (it.active) canvas.drawCircle(it.x, it.y, it.radius, pickupPaint)
        }

        canvas.drawText("Score: ${world.score}", 32f, 56f, textPaint)
        canvas.drawText("Best: ${world.bestScore}", 32f, 104f, textPaint)

        when (world.state) {
            GameState.MENU -> canvas.drawText("Tap to Play", width * 0.35f, height * 0.5f, textPaint)
            GameState.GAME_OVER -> canvas.drawText("Game Over - Tap Retry", width * 0.2f, height * 0.5f, textPaint)
            GameState.PAUSED -> canvas.drawText("Paused", width * 0.42f, height * 0.5f, textPaint)
            GameState.PLAYING -> Unit
        }

        canvas.drawText("FPS: ${debugStats.fps}", 32f, height - 56f, debugPaint)
        canvas.drawText("Frame: ${debugStats.frameTimeMs.roundToInt()}ms", 180f, height - 56f, debugPaint)
    }

    private fun updateDebugStats(fps: Int, frameMs: Float) {
        debugStats = DebugStats(fps = fps, frameTimeMs = frameMs)
    }

    private fun handleRunEnded(event: RunEndEvent) {
        analytics.log(
            "run_end",
            mapOf(
                "duration_sec" to event.durationSec,
                "score" to event.score,
                "death_reason" to event.deathReason
            )
        )
        onRunEnded(event)
    }
}
