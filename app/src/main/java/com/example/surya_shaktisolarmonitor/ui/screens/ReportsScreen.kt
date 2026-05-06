package com.example.surya_shaktisolarmonitor.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.surya_shaktisolarmonitor.ui.components.SolarCard
import com.example.surya_shaktisolarmonitor.ui.theme.ChartExport
import com.example.surya_shaktisolarmonitor.ui.theme.ChartGrid
import com.example.surya_shaktisolarmonitor.ui.theme.ChartSavings
import com.example.surya_shaktisolarmonitor.ui.theme.ChartSolar
import com.example.surya_shaktisolarmonitor.ui.theme.SolarBlack
import com.example.surya_shaktisolarmonitor.ui.theme.SolarDarkGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarGreen
import com.example.surya_shaktisolarmonitor.ui.theme.SolarLightGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarTextGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarWhite
import com.example.surya_shaktisolarmonitor.ui.theme.SolarYellow
import com.example.surya_shaktisolarmonitor.utils.SimulationUtils
import com.example.surya_shaktisolarmonitor.viewmodel.SolarViewModel
import kotlinx.coroutines.delay

/**
 * Reports Screen — View 30-day energy history, savings, and exports.
 *
 * Features:
 * - Summary statistics at the top
 * - Bar chart visualization of generation vs consumption
 * - Detailed log table with all data points
 * - Exported energy tracking
 */
@Composable
fun ReportsScreen(viewModel: SolarViewModel) {
    val last30DaysLogs by viewModel.last30DaysLogs.collectAsState()
    val totalGeneration by viewModel.totalGeneration.collectAsState()
    val totalConsumption by viewModel.totalConsumption.collectAsState()
    val totalSavings by viewModel.totalSavings.collectAsState()
    val totalExported by viewModel.totalExported.collectAsState()

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
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
                Text(
                    text = "📊 Energy Reports",
                    style = MaterialTheme.typography.headlineLarge,
                    color = SolarYellow,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Last 30 days energy history",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SolarTextGray
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // ── Summary Cards ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { -20 }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Gen",
                        value = "${String.format("%.0f", totalGeneration)} kWh",
                        color = ChartSolar
                    )
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Used",
                        value = "${String.format("%.0f", totalConsumption)} kWh",
                        color = ChartGrid
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        title = "Savings",
                        value = "₹${String.format("%.0f", totalSavings)}",
                        color = ChartSavings
                    )
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        title = "Exported",
                        value = "${String.format("%.0f", totalExported)} kWh",
                        color = ChartExport
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Bar Chart ──
        AnimatedVisibility(
            visible = isVisible && last30DaysLogs.isNotEmpty(),
            enter = fadeIn() + slideInVertically { 0 }
        ) {
            SolarCard(hasAccentBorder = true) {
                Text(
                    text = "Generation vs Consumption",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Daily comparison (kWh)",
                    style = MaterialTheme.typography.bodySmall,
                    color = SolarTextGray
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Simple bar chart
                val chartData = last30DaysLogs.reversed().takeLast(15) // Show last 15 for readability
                val maxValue = chartData.maxOfOrNull {
                    maxOf(it.generationKwh, it.consumptionKwh)
                }?.toFloat()?.coerceAtLeast(1f) ?: 1f

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SolarBlack.copy(alpha = 0.5f))
                        .padding(8.dp)
                ) {
                    val barWidth = (size.width / (chartData.size * 2.5f)).coerceAtMost(20f)
                    val gap = barWidth * 0.3f
                    val totalBarGroupWidth = (barWidth * 2) + gap
                    val startX = (size.width - (totalBarGroupWidth * chartData.size)) / 2f

                    chartData.forEachIndexed { index, log ->
                        val x = startX + (index * totalBarGroupWidth)

                        // Generation bar (yellow)
                        val genHeight = (log.generationKwh.toFloat() / maxValue) * size.height * 0.85f
                        drawRect(
                            color = ChartSolar,
                            topLeft = Offset(x, size.height - genHeight),
                            size = Size(barWidth, genHeight)
                        )

                        // Consumption bar (red)
                        val conHeight = (log.consumptionKwh.toFloat() / maxValue) * size.height * 0.85f
                        drawRect(
                            color = ChartGrid.copy(alpha = 0.7f),
                            topLeft = Offset(x + barWidth + gap, size.height - conHeight),
                            size = Size(barWidth, conHeight)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ChartLegend(color = ChartSolar, label = "Generation")
                    Spacer(modifier = Modifier.width(24.dp))
                    ChartLegend(color = ChartGrid, label = "Consumption")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Detailed Log Table ──
        AnimatedVisibility(
            visible = isVisible && last30DaysLogs.isNotEmpty(),
            enter = fadeIn() + slideInVertically { 20 }
        ) {
            SolarCard {
                Text(
                    text = "📋 Detailed History",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarWhite
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Table Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Date", style = MaterialTheme.typography.labelSmall, color = SolarTextGray, modifier = Modifier.weight(1.1f))
                    Text("🌤", style = MaterialTheme.typography.labelSmall, color = SolarTextGray, modifier = Modifier.weight(0.5f))
                    Text("Gen", style = MaterialTheme.typography.labelSmall, color = SolarTextGray, modifier = Modifier.weight(0.8f))
                    Text("Used", style = MaterialTheme.typography.labelSmall, color = SolarTextGray, modifier = Modifier.weight(0.8f))
                    Text("Saved", style = MaterialTheme.typography.labelSmall, color = SolarTextGray, modifier = Modifier.weight(0.8f))
                    Text("Export", style = MaterialTheme.typography.labelSmall, color = SolarTextGray, modifier = Modifier.weight(0.8f))
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 6.dp),
                    color = SolarLightGray.copy(alpha = 0.3f)
                )

                // Log rows
                last30DaysLogs.forEach { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = log.date.takeLast(5),
                            style = MaterialTheme.typography.bodySmall,
                            color = SolarWhite,
                            modifier = Modifier.weight(1.1f)
                        )
                        Text(
                            text = SimulationUtils.getWeatherEmoji(log.weather),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(0.5f)
                        )
                        Text(
                            text = String.format("%.1f", log.generationKwh),
                            style = MaterialTheme.typography.bodySmall,
                            color = SolarYellow,
                            modifier = Modifier.weight(0.8f)
                        )
                        Text(
                            text = String.format("%.1f", log.consumptionKwh),
                            style = MaterialTheme.typography.bodySmall,
                            color = SolarWhite,
                            modifier = Modifier.weight(0.8f)
                        )
                        Text(
                            text = "₹${String.format("%.0f", log.savingsRupees)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SolarGreen,
                            modifier = Modifier.weight(0.8f)
                        )
                        Text(
                            text = if (log.exportedKwh > 0) String.format("%.1f", log.exportedKwh) else "-",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (log.exportedKwh > 0) ChartExport else SolarTextGray,
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                }
            }
        }

        // ── Empty State ──
        if (last30DaysLogs.isEmpty()) {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn()
            ) {
                SolarCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📊",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Data Yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = SolarWhite
                        )
                        Text(
                            text = "Start logging generation and consumption to see reports here.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SolarTextGray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * Summary mini-card for report overview.
 */
@Composable
private fun SummaryMiniCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    SolarCard(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = SolarTextGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Chart legend item.
 */
@Composable
private fun ChartLegend(
    color: androidx.compose.ui.graphics.Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SolarTextGray
        )
    }
}
