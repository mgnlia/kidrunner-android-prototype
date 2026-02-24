package com.gz.kidsafe.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

/**
 * Composable card that renders a live monetization + parental-gate KPI dashboard.
 * Accepts a [MonetizationKpiSnapshot] so callers control refresh cadence.
 */
@Composable
fun MonetizationKpiDashboardCard(
    modifier: Modifier = Modifier,
    snapshot: MonetizationKpiSnapshot
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Monetization KPI Dashboard",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))
            Divider()
            Spacer(modifier = Modifier.height(4.dp))

            // --- Ad Init ---
            SectionHeader("Ad Initialization")
            KpiRow("Init Attempts", snapshot.initAttempts.toString())
            KpiRow("Init Allowed", snapshot.initAllowed.toString())
            KpiRow("Init Blocked", snapshot.initBlocked.toString())

            Spacer(modifier = Modifier.height(4.dp))
            Divider()
            Spacer(modifier = Modifier.height(4.dp))

            // --- Banner ---
            SectionHeader("Banner Ads")
            KpiRow("Eligibility Allowed", snapshot.bannerEligibilityAllowed.toString())
            KpiRow("Eligibility Blocked", snapshot.bannerEligibilityBlocked.toString())
            KpiRow("Block Rate", snapshot.bannerBlockRate.toPercent())
            KpiRow("Requests", snapshot.bannerRequests.toString())
            KpiRow("Loads", snapshot.bannerLoads.toString())
            KpiRow("Load Rate", snapshot.bannerLoadRate.toPercent())
            KpiRow("Load Failures", snapshot.bannerLoadFailures.toString())
            KpiRow("Impressions", snapshot.bannerImpressions.toString())
            KpiRow("Clicks", snapshot.bannerClicks.toString())
            KpiRow("CTR", snapshot.bannerClickThroughRate.toPercent())

            Spacer(modifier = Modifier.height(4.dp))
            Divider()
            Spacer(modifier = Modifier.height(4.dp))

            // --- Parental Gate ---
            SectionHeader("Parental Gate")
            KpiRow("Prompted", snapshot.parentGatePrompted.toString())
            KpiRow("Passed", snapshot.parentGatePassed.toString())
            KpiRow("Failed", snapshot.parentGateFailed.toString())
            KpiRow("Success Rate", snapshot.parentGateSuccessRate.toPercent())

            // --- Block Reasons ---
            if (snapshot.blockedByReason.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Divider()
                Spacer(modifier = Modifier.height(4.dp))
                SectionHeader("Block Reasons")
                snapshot.blockedByReason.entries
                    .sortedByDescending { it.value }
                    .forEach { (reason, count) ->
                        KpiRow(reason.name, count.toString())
                    }
            }

            // --- Recent Events ---
            val recent = snapshot.recentEvents.takeLast(5)
            if (recent.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Divider()
                Spacer(modifier = Modifier.height(4.dp))
                SectionHeader("Recent Events (last 5)")
                recent.reversed().forEach { event ->
                    Text(
                        text = "• ${event.type.name}" + (event.details?.let { " – $it" } ?: ""),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun KpiRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
    }
}

private fun Double.toPercent(): String =
    String.format(Locale.US, "%.1f%%", this * 100.0)
