package com.example.surya_shaktisolarmonitor.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Battery5Bar
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.SolarPower
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.surya_shaktisolarmonitor.ui.components.SolarCard
import com.example.surya_shaktisolarmonitor.ui.components.SolarCircularProgress
import com.example.surya_shaktisolarmonitor.ui.theme.ChartGrid
import com.example.surya_shaktisolarmonitor.ui.theme.ChartSolar
import com.example.surya_shaktisolarmonitor.ui.theme.SolarAmber
import com.example.surya_shaktisolarmonitor.ui.theme.SolarBlack
import com.example.surya_shaktisolarmonitor.ui.theme.SolarBlue
import com.example.surya_shaktisolarmonitor.ui.theme.SolarGreen
import com.example.surya_shaktisolarmonitor.ui.theme.SolarTextGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarWhite
import com.example.surya_shaktisolarmonitor.ui.theme.SolarYellow
import com.example.surya_shaktisolarmonitor.ui.theme.SolarYellowDark
import com.example.surya_shaktisolarmonitor.viewmodel.SolarViewModel
import kotlinx.coroutines.delay

/**
 * Dashboard Screen — Main overview of solar energy system.
 *
 * Displays:
 * - Total Solar Energy Generated
 * - Total Energy Consumed
 * - Net Savings in ₹
 * - Battery Level
 * - Green Energy Independence Score
 * - Circular progress bar (Solar vs Grid usage)
 */
@Composable
fun DashboardScreen(viewModel: SolarViewModel) {
    val totalGeneration by viewModel.totalGeneration.collectAsState()
    val totalConsumption by viewModel.totalConsumption.collectAsState()
    val totalSavings by viewModel.totalSavings.collectAsState()
    val batteryLevel by viewModel.batteryLevel.collectAsState()
    val independenceScore by viewModel.independenceScore.collectAsState()
    val totalExported by viewModel.totalExported.collectAsState()
    val latestLog by viewModel.latestLog.collectAsState()

    // Animation trigger
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    // Calculate solar percentage for the circular indicator
    val solarPercentage = if (totalConsumption > 0) {
        ((totalGeneration / totalConsumption) * 100).toFloat().coerceIn(0f, 100f)
    } else if (totalGeneration > 0) {
        100f
    } else {
        0f
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SolarBlack)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // ── Header ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { -40 }
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WbSunny,
                        contentDescription = "Sun",
                        tint = SolarYellow,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Surya-Shakti",
                            style = MaterialTheme.typography.headlineLarge,
                            color = SolarYellow,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Solar Energy Dashboard",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SolarTextGray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // ── Circular Progress — Solar vs Grid ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { -20 }
        ) {
            SolarCard(hasAccentBorder = true) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Solar vs Grid Usage",
                        style = MaterialTheme.typography.titleMedium,
                        color = SolarWhite
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    SolarCircularProgress(
                        solarPercentage = solarPercentage,
                        size = 200.dp,
                        strokeWidth = 18.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LegendItem(color = ChartSolar, label = "Solar", value = "${solarPercentage.toInt()}%")
                        LegendItem(color = ChartGrid, label = "Grid", value = "${(100 - solarPercentage.toInt())}%")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Stats Grid ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { 20 }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Row 1: Generation & Consumption
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.SolarPower,
                        iconTint = SolarYellow,
                        title = "Generated",
                        value = "${String.format("%.1f", totalGeneration)} kWh",
                        subtitle = "Total Solar Energy"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.ElectricBolt,
                        iconTint = SolarAmber,
                        title = "Consumed",
                        value = "${String.format("%.1f", totalConsumption)} kWh",
                        subtitle = "Total Usage"
                    )
                }

                // Row 2: Savings & Battery
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.Savings,
                        iconTint = SolarGreen,
                        title = "Savings",
                        value = "₹${String.format("%.0f", totalSavings)}",
                        subtitle = "Total Saved"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = if (batteryLevel > 50) Icons.Rounded.BatteryChargingFull else Icons.Rounded.Battery5Bar,
                        iconTint = if (batteryLevel > 50) SolarGreen else SolarAmber,
                        title = "Battery",
                        value = "$batteryLevel%",
                        subtitle = "Charge Level"
                    )
                }

                // Row 3: Independence Score & Exported
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.Eco,
                        iconTint = SolarGreen,
                        title = "Green Score",
                        value = "${String.format("%.0f", independenceScore)}%",
                        subtitle = "Independence"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.Bolt,
                        iconTint = SolarBlue,
                        title = "Exported",
                        value = "${String.format("%.1f", totalExported)} kWh",
                        subtitle = "To Grid"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Latest Log Summary ──
        AnimatedVisibility(
            visible = isVisible && latestLog != null,
            enter = fadeIn() + slideInVertically { 40 }
        ) {
            latestLog?.let { log ->
                SolarCard(hasAccentBorder = true) {
                    Text(
                        text = "📊 Latest Entry — ${log.date}",
                        style = MaterialTheme.typography.titleMedium,
                        color = SolarYellow
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Weather", style = MaterialTheme.typography.labelSmall, color = SolarTextGray)
                            Text(
                                "${com.example.surya_shaktisolarmonitor.utils.SimulationUtils.getWeatherEmoji(log.weather)} ${log.weather}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = SolarWhite
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Net Usage", style = MaterialTheme.typography.labelSmall, color = SolarTextGray)
                            Text(
                                "${String.format("%.1f", log.netUsageKwh)} kWh",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (log.netUsageKwh <= 0) SolarGreen else SolarAmber
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp)) // Bottom nav clearance
    }
}

/**
 * Individual stat card used in the dashboard grid.
 */
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    title: String,
    value: String,
    subtitle: String
) {
    SolarCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(iconTint.copy(alpha = 0.2f), iconTint.copy(alpha = 0.05f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = SolarTextGray
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    color = SolarWhite,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = SolarTextGray.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * Legend item for the circular progress chart.
 */
@Composable
private fun LegendItem(
    color: androidx.compose.ui.graphics.Color,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodySmall,
            color = SolarTextGray
        )
    }
}
