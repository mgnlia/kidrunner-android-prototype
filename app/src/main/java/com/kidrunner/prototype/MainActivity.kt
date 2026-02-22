package com.kidrunner.prototype

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kidrunner.prototype.ads.AdFeatureFlags
import com.kidrunner.prototype.ads.AdRuntimeProfile
import com.kidrunner.prototype.ads.KidSafeAdController
import com.kidrunner.prototype.ads.KidSafeAdPolicy
import com.kidrunner.prototype.ads.NoopAdSdkAdapter
import com.kidrunner.prototype.analytics.AndroidLogAnalyticsLogger
import com.kidrunner.prototype.game.GameView
import com.kidrunner.prototype.game.RunEndEvent

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val analytics = AndroidLogAnalyticsLogger()

        // Compliance-by-design default: child safe mode ON, ads OFF until policy+console checks pass.
        val adController = KidSafeAdController(
            policy = KidSafeAdPolicy(),
            adapter = NoopAdSdkAdapter(),
            analytics = analytics,
            featureFlags = AdFeatureFlags(
                adsEnabled = false,
                rewardedContinueEnabled = false,
                childSafeMode = true,
                allowUnknownAgeMonetization = false
            )
        )

        adController.initialize(
            AdRuntimeProfile(
                appInFamiliesProgram = true,
                includesChildrenAudience = true,
                mixedAudience = false,
                userAgeKnown = false,
                isUnderAge = true,
                isEea = false,
                mediationNetworksFamiliesCertified = false
            )
        )

        gameView = GameView(
            context = this,
            analytics = analytics,
            onRunEnded = { _: RunEndEvent ->
                // Ad showing is policy-gated and no-op when compliance/flags fail.
                adController.maybeShowInterstitial()
            }
        )
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        gameView.pause()
        super.onPause()
    }
}
