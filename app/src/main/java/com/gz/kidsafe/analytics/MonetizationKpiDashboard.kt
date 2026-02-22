package com.gz.kidsafe.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun MonetizationKpiDashboardCard(
    snapshot: MonetizationKpiSnapshot,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Monetization KPI Dashboard",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            KpiLine("Ad init attempts", snapshot.initAttempts.toString())
            KpiLine("Ad init allowed", snapshot.initAllowed.toString())
            KpiLine("Ad init blocked", snapshot.initBlocked.toString())
            KpiLine("Banner eligible", snapshot.bannerEligibilityAllowed.toString())
            KpiLine("Banner blocked", snapshot.bannerEligibilityBlocked.toString())
            KpiLine("Banner requests", snapshot.bannerRequests.toString())
            KpiLine("Banner loads", snapshot.bannerLoads.toString())
            KpiLine("Banner load failures", snapshot.bannerLoadFailures.toString())
            KpiLine("Banner impressions", snapshot.bannerImpressions.toString())
            KpiLine("Banner clicks", snapshot.bannerClicks.toString())
            KpiLine("Banner load rate", formatPercentage(snapshot.bannerLoadRate))
            KpiLine("Banner CTR", formatPercentage(snapshot.bannerClickThroughRate))
            KpiLine("Banner block rate", formatPercentage(snapshot.bannerBlockRate))
            KpiLine("Parent gate prompted", snapshot.parentGatePrompted.toString())
            KpiLine("Parent gate passed", snapshot.parentGatePassed.toString())
            KpiLine("Parent gate failed", snapshot.parentGateFailed.toString())
            KpiLine("Parent gate success", formatPercentage(snapshot.parentGateSuccessRate))

            if (snapshot.blockedByReason.isNotEmpty()) {
                Text(
                    text = "Block reasons",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                snapshot.blockedByReason
                    .toList()
                    .sortedByDescending { it.second }
                    .forEach { (reason, count) ->
                        KpiLine(reason.name, count.toString())
                    }
            }
        }
    }
}

@Composable
private fun KpiLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
    }
}

private fun formatPercentage(value: Double): String {
    return String.format(Locale.US, "%.1f%%", value * 100.0)
}
