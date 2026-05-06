package com.example.surya_shaktisolarmonitor.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.surya_shaktisolarmonitor.ui.theme.ChartGrid
import com.example.surya_shaktisolarmonitor.ui.theme.ChartSolar
import com.example.surya_shaktisolarmonitor.ui.theme.SolarTextGray

/**
 * Custom Circular Progress Indicator showing Solar vs Grid usage.
 *
 * Displays a two-tone arc:
 * - Yellow arc = Solar energy percentage
 * - Red arc = Grid energy percentage
 * - Center text = Solar percentage value
 *
 * Features smooth animation when values change.
 *
 * @param solarPercentage Percentage of energy from solar (0-100)
 * @param size Diameter of the progress indicator
 * @param strokeWidth Width of the arc stroke
 * @param solarColor Color for solar portion (default: yellow)
 * @param gridColor Color for grid portion (default: red)
 */
@Composable
fun SolarCircularProgress(
    solarPercentage: Float,
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    strokeWidth: Dp = 16.dp,
    solarColor: Color = ChartSolar,
    gridColor: Color = ChartGrid
) {
    // Clamp the percentage to valid range
    val clampedPercentage = solarPercentage.coerceIn(0f, 100f)

    // Animate the progress for smooth transitions
    var targetProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(clampedPercentage) {
        targetProgress = clampedPercentage
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1200),
        label = "solar_progress"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasSize = this.size.minDimension
            val stroke = strokeWidth.toPx()
            val arcSize = Size(canvasSize - stroke, canvasSize - stroke)
            val topLeft = Offset(stroke / 2f, stroke / 2f)

            // Background track (subtle gray)
            drawArc(
                color = Color(0xFF2A2A2A),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            // Grid usage arc (red) — draws the full background portion
            val gridSweep = 360f * ((100f - animatedProgress) / 100f)
            if (gridSweep > 0f) {
                drawArc(
                    color = gridColor.copy(alpha = 0.6f),
                    startAngle = -90f + (360f * animatedProgress / 100f),
                    sweepAngle = gridSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }

            // Solar usage arc (yellow) — foreground
            val solarSweep = 360f * (animatedProgress / 100f)
            if (solarSweep > 0f) {
                drawArc(
                    color = solarColor,
                    startAngle = -90f,
                    sweepAngle = solarSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
        }

        // Center text showing solar percentage
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${animatedProgress.toInt()}%",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                color = solarColor
            )
            Text(
                text = "Solar",
                style = MaterialTheme.typography.labelMedium,
                color = SolarTextGray
            )
        }
    }
}
