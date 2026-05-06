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
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.surya_shaktisolarmonitor.ui.theme.SolarBlue
import com.example.surya_shaktisolarmonitor.ui.theme.SolarGreen
import com.example.surya_shaktisolarmonitor.ui.theme.SolarLightGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarTextGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarWhite
import com.example.surya_shaktisolarmonitor.ui.theme.SolarYellow
import com.example.surya_shaktisolarmonitor.viewmodel.SolarViewModel
import kotlinx.coroutines.delay

/**
 * Consumption Tracker Screen — Enter meter readings and track energy usage.
 *
 * Features:
 * - Previous and current meter reading inputs
 * - Auto-calculation of daily consumption
 * - Net energy usage calculation
 * - Export detection (when generation > consumption)
 * - Visual breakdown of energy flow
 */
@Composable
fun ConsumptionScreen(viewModel: SolarViewModel) {
    val currentConsumption by viewModel.currentConsumption.collectAsState()
    val simulatedGeneration by viewModel.simulatedGeneration.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    var previousReading by remember { mutableStateOf("") }
    var currentReading by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    // Calculated values
    val netUsage = currentConsumption - simulatedGeneration
    val exportedEnergy = if (simulatedGeneration > currentConsumption) {
        simulatedGeneration - currentConsumption
    } else 0.0

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
                    text = "⚡ Consumption Tracker",
                    style = MaterialTheme.typography.headlineLarge,
                    color = SolarYellow,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Track your daily electricity consumption",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SolarTextGray
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // ── Meter Readings ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { -20 }
        ) {
            SolarCard(hasAccentBorder = true) {
                Text(
                    text = "Enter Meter Readings",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarWhite
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = previousReading,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                            previousReading = input
                            input.toDoubleOrNull()?.let { viewModel.updatePreviousReading(it) }
                        }
                    },
                    label = { Text("Previous Reading (kWh)") },
                    placeholder = { Text("e.g., 1250") },
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

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = currentReading,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                            currentReading = input
                            input.toDoubleOrNull()?.let { viewModel.updateCurrentReading(it) }
                        }
                    },
                    label = { Text("Current Reading (kWh)") },
                    placeholder = { Text("e.g., 1265") },
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
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Consumption Breakdown ──
        AnimatedVisibility(
            visible = isVisible && currentConsumption > 0,
            enter = fadeIn() + slideInVertically { 0 }
        ) {
            SolarCard {
                Text(
                    text = "Energy Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarWhite
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Daily Consumption
                EnergyRow(
                    label = "Daily Consumption",
                    value = "${String.format("%.1f", currentConsumption)} kWh",
                    icon = Icons.Rounded.ArrowDownward,
                    color = SolarAmber
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = SolarLightGray.copy(alpha = 0.3f)
                )

                // Solar Generation
                EnergyRow(
                    label = "Solar Generation",
                    value = "${String.format("%.1f", simulatedGeneration)} kWh",
                    icon = Icons.Rounded.ArrowUpward,
                    color = SolarYellow
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = SolarLightGray.copy(alpha = 0.3f)
                )

                // Net Usage
                EnergyRow(
                    label = "Net Usage (from Grid)",
                    value = "${String.format("%.1f", netUsage.coerceAtLeast(0.0))} kWh",
                    icon = Icons.Rounded.ArrowDownward,
                    color = if (netUsage > 0) SolarAmber else SolarGreen
                )

                // Exported Energy (if applicable)
                if (exportedEnergy > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = SolarLightGray.copy(alpha = 0.3f)
                    )

                    EnergyRow(
                        label = "Exported to Grid 🎉",
                        value = "${String.format("%.1f", exportedEnergy)} kWh",
                        icon = Icons.Rounded.ArrowUpward,
                        color = SolarGreen
                    )
                }

                // Status message
                Spacer(modifier = Modifier.height(12.dp))
                val statusText = if (netUsage <= 0) {
                    "🌿 Great! You're producing more than you consume!"
                } else {
                    "⚡ You're drawing ${String.format("%.1f", netUsage)} kWh from the grid."
                }
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (netUsage <= 0) SolarGreen else SolarTextGray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Log Button ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { 40 }
        ) {
            Button(
                onClick = {
                    viewModel.logConsumption()
                    previousReading = ""
                    currentReading = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SolarYellow,
                    contentColor = SolarBlack
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = currentConsumption > 0
            ) {
                Icon(Icons.Rounded.Save, contentDescription = "Save")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log Consumption",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ── Status Message ──
        if (statusMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            SolarCard {
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SolarGreen
                )
            }
            LaunchedEffect(statusMessage) {
                delay(3000)
                viewModel.clearStatusMessage()
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * Energy breakdown row item.
 */
@Composable
private fun EnergyRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = SolarTextGray
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
