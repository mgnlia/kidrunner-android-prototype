package com.gz.kidsafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gz.kidsafe.ads.AgeSignal
import com.gz.kidsafe.ads.KidSafeBannerAdView
import com.gz.kidsafe.policy.ParentalGate
import com.gz.kidsafe.ui.theme.KidSafePrototypeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KidSafePrototypeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    KidSafeHome()
                }
            }
        }
    }
}

@Composable
private fun KidSafeHome() {
    val gateOpen = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("KidSafe Prototype", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Families/COPPA mode is always ON. Ads are contextual/test only and kid-safe constrained.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        KidSafeBannerAdView(
            modifier = Modifier.fillMaxWidth(),
            ageSignal = AgeSignal.UNKNOWN
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { gateOpen.value = true }) {
            Text("Parent Area")
        }

        if (gateOpen.value) {
            ParentalGate(
                onSuccess = { gateOpen.value = false },
                onDismiss = { gateOpen.value = false }
            )
        }
    }
}
