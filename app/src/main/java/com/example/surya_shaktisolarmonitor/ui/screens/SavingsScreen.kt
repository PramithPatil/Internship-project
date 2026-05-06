package com.example.surya_shaktisolarmonitor.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.surya_shaktisolarmonitor.ui.components.SolarCard
import com.example.surya_shaktisolarmonitor.ui.theme.SolarAmber
import com.example.surya_shaktisolarmonitor.ui.theme.SolarBlack
import com.example.surya_shaktisolarmonitor.ui.theme.SolarGreen
import com.example.surya_shaktisolarmonitor.ui.theme.SolarLightGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarTextGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarWhite
import com.example.surya_shaktisolarmonitor.ui.theme.SolarYellow
import com.example.surya_shaktisolarmonitor.utils.SimulationUtils
import com.example.surya_shaktisolarmonitor.viewmodel.SolarViewModel
import kotlinx.coroutines.delay

/**
 * Savings Calculator Screen — Track electricity bill savings from solar power.
 *
 * Features:
 * - Configurable per-unit electricity rate
 * - Daily savings display
 * - Monthly savings (30-day) summary
 * - Total savings history
 * - Recent savings log
 */
@Composable
fun SavingsScreen(viewModel: SolarViewModel) {
    val totalSavings by viewModel.totalSavings.collectAsState()
    val monthlySavings by viewModel.monthlySavings.collectAsState()
    val totalGeneration by viewModel.totalGeneration.collectAsState()
    val ratePerUnit by viewModel.ratePerUnit.collectAsState()
    val last30DaysLogs by viewModel.last30DaysLogs.collectAsState()
    val latestLog by viewModel.latestLog.collectAsState()

    var rateInput by remember { mutableStateOf(ratePerUnit.toString()) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    // Calculate daily savings from latest log
    val dailySavings = latestLog?.savingsRupees ?: 0.0

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
                    text = "💰 Savings Calculator",
                    style = MaterialTheme.typography.headlineLarge,
                    color = SolarYellow,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Track your electricity bill savings",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SolarTextGray
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // ── Per Unit Rate Config ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { -20 }
        ) {
            SolarCard {
                Text(
                    text = "Electricity Rate",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarWhite
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = rateInput,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                            rateInput = input
                            input.toDoubleOrNull()?.let { viewModel.updateRatePerUnit(it) }
                        }
                    },
                    label = { Text("Rate per Unit (₹/kWh)") },
                    placeholder = { Text("e.g., 8.0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SolarYellow,
                        unfocusedBorderColor = SolarLightGray,
                        focusedLabelColor = SolarYellow,
                        unfocusedLabelColor = SolarTextGray,
                        cursorColor = SolarYellow,
                        focusedTextColor = SolarWhite,
                        unfocusedTextColor = SolarWhite
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Formula: Savings = Solar Units × ₹$rateInput per unit",
                    style = MaterialTheme.typography.bodySmall,
                    color = SolarTextGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Savings Overview Cards ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { 0 }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Daily Savings
                SolarCard(hasAccentBorder = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Today's Savings",
                                style = MaterialTheme.typography.labelMedium,
                                color = SolarTextGray
                            )
                            Text(
                                text = "₹${String.format("%.0f", dailySavings)}",
                                style = MaterialTheme.typography.displaySmall,
                                color = SolarGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.TrendingUp,
                            contentDescription = "Savings",
                            tint = SolarGreen,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // Monthly Savings
                SolarCard(hasAccentBorder = true) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Monthly Savings (30 days)",
                                style = MaterialTheme.typography.labelMedium,
                                color = SolarTextGray
                            )
                            Text(
                                text = "₹${String.format("%.0f", monthlySavings)}",
                                style = MaterialTheme.typography.displaySmall,
                                color = SolarYellow,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.CalendarMonth,
                            contentDescription = "Monthly",
                            tint = SolarYellow,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // Total Savings
                SolarCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total Savings (All Time)",
                                style = MaterialTheme.typography.labelMedium,
                                color = SolarTextGray
                            )
                            Text(
                                text = "₹${String.format("%.0f", totalSavings)}",
                                style = MaterialTheme.typography.displaySmall,
                                color = SolarAmber,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "From ${String.format("%.1f", totalGeneration)} kWh solar generation",
                                style = MaterialTheme.typography.bodySmall,
                                color = SolarTextGray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Recent Savings History ──
        AnimatedVisibility(
            visible = isVisible && last30DaysLogs.isNotEmpty(),
            enter = fadeIn() + slideInVertically { 20 }
        ) {
            SolarCard {
                Text(
                    text = "📊 Recent Savings History",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarWhite
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Date", style = MaterialTheme.typography.labelSmall, color = SolarTextGray, modifier = Modifier.weight(1.2f))
                    Text("Gen (kWh)", style = MaterialTheme.typography.labelSmall, color = SolarTextGray, modifier = Modifier.weight(1f))
                    Text("Savings (₹)", style = MaterialTheme.typography.labelSmall, color = SolarTextGray, modifier = Modifier.weight(1f))
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 6.dp),
                    color = SolarLightGray.copy(alpha = 0.3f)
                )

                // Log entries (show last 10)
                last30DaysLogs.take(10).forEach { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = log.date.takeLast(5), // Show MM-DD
                            style = MaterialTheme.typography.bodySmall,
                            color = SolarWhite,
                            modifier = Modifier.weight(1.2f)
                        )
                        Text(
                            text = String.format("%.1f", log.generationKwh),
                            style = MaterialTheme.typography.bodySmall,
                            color = SolarYellow,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "₹${String.format("%.0f", log.savingsRupees)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SolarGreen,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}
