package com.example.surya_shaktisolarmonitor.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.surya_shaktisolarmonitor.ui.components.SolarCard
import com.example.surya_shaktisolarmonitor.ui.theme.SolarAmber
import com.example.surya_shaktisolarmonitor.ui.theme.SolarBlack
import com.example.surya_shaktisolarmonitor.ui.theme.SolarBlue
import com.example.surya_shaktisolarmonitor.ui.theme.SolarDarkGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarGreen
import com.example.surya_shaktisolarmonitor.ui.theme.SolarLightGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarTextGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarWhite
import com.example.surya_shaktisolarmonitor.ui.theme.SolarYellow
import com.example.surya_shaktisolarmonitor.ui.theme.SolarYellowDark
import com.example.surya_shaktisolarmonitor.utils.SimulationUtils
import com.example.surya_shaktisolarmonitor.viewmodel.SolarViewModel
import kotlinx.coroutines.delay

/**
 * Generation Log Screen — Manual entry of daily solar generation.
 *
 * Features:
 * - Weather condition selector (Sunny/Cloudy/Rainy)
 * - Auto-simulation of generation based on weather
 * - Manual generation value entry
 * - Log generation to database
 * - Visual feedback with animated cards
 */
@Composable
fun GenerationLogScreen(viewModel: SolarViewModel) {
    val selectedWeather by viewModel.selectedWeather.collectAsState()
    val simulatedGeneration by viewModel.simulatedGeneration.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    var manualGeneration by remember { mutableStateOf("") }
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
                    text = "☀\uFE0F Generation Log",
                    style = MaterialTheme.typography.headlineLarge,
                    color = SolarYellow,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Log your daily solar power generation",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SolarTextGray
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // ── Weather Selector ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { -20 }
        ) {
            SolarCard(hasAccentBorder = true) {
                Text(
                    text = "Select Weather Condition",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarWhite
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherOption(
                        icon = Icons.Rounded.WbSunny,
                        label = "Sunny",
                        isSelected = selectedWeather == "Sunny",
                        color = SolarYellow,
                        onClick = { viewModel.updateWeather("Sunny") }
                    )
                    WeatherOption(
                        icon = Icons.Rounded.Cloud,
                        label = "Cloudy",
                        isSelected = selectedWeather == "Cloudy",
                        color = SolarTextGray,
                        onClick = { viewModel.updateWeather("Cloudy") }
                    )
                    WeatherOption(
                        icon = Icons.Rounded.WaterDrop,
                        label = "Rainy",
                        isSelected = selectedWeather == "Rainy",
                        color = SolarBlue,
                        onClick = { viewModel.updateWeather("Rainy") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Simulation Result ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { 0 }
        ) {
            SolarCard {
                Text(
                    text = "Simulated Generation",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarWhite
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${String.format("%.2f", simulatedGeneration)} kWh",
                            style = MaterialTheme.typography.displaySmall,
                            color = SolarYellow,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${SimulationUtils.getWeatherEmoji(selectedWeather)} $selectedWeather conditions",
                            style = MaterialTheme.typography.bodySmall,
                            color = SolarTextGray
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.simulateGeneration() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SolarYellow
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SolarYellow)
                    ) {
                        Text("Simulate")
                    }
                }

                // Generation quality indicator
                Spacer(modifier = Modifier.height(12.dp))
                val quality = when {
                    simulatedGeneration >= 12 -> "🔋 Excellent Generation"
                    simulatedGeneration >= 5 -> "⚡ Moderate Generation"
                    simulatedGeneration > 0 -> "🔌 Low Generation"
                    else -> "⏳ Press Simulate"
                }
                val qualityColor = when {
                    simulatedGeneration >= 12 -> SolarGreen
                    simulatedGeneration >= 5 -> SolarAmber
                    simulatedGeneration > 0 -> SolarBlue
                    else -> SolarTextGray
                }
                Text(
                    text = quality,
                    style = MaterialTheme.typography.bodyMedium,
                    color = qualityColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Manual Entry ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { 20 }
        ) {
            SolarCard {
                Text(
                    text = "Manual Entry (Optional)",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolarWhite
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = manualGeneration,
                    onValueChange = { input ->
                        // Allow only valid numeric input
                        if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                            manualGeneration = input
                        }
                    },
                    label = { Text("Generation (kWh)") },
                    placeholder = { Text("e.g., 15.5") },
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

        Spacer(modifier = Modifier.height(20.dp))

        // ── Log Button ──
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically { 40 }
        ) {
            Button(
                onClick = {
                    val manualValue = manualGeneration.toDoubleOrNull()
                    viewModel.logGeneration(manualValue)
                    manualGeneration = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SolarYellow,
                    contentColor = SolarBlack
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Log")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log Generation",
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
 * Selectable weather option button.
 */
@Composable
private fun WeatherOption(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(
                if (isSelected) color.copy(alpha = 0.15f) else SolarDarkGray
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) color else SolarLightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) color else SolarTextGray,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) color else SolarTextGray
        )
    }
}
