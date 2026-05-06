package com.example.surya_shaktisolarmonitor.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.surya_shaktisolarmonitor.ui.theme.SolarDarkGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarLightGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarYellow

/**
 * Reusable solar-themed card component.
 *
 * Features:
 * - Rounded corners (16dp) for modern look
 * - Dark background with subtle yellow border
 * - Smooth content size animation
 * - Consistent padding
 *
 * @param modifier External modifier
 * @param hasAccentBorder Whether to show a yellow accent border
 * @param content Card content composable
 */
@Composable
fun SolarCard(
    modifier: Modifier = Modifier,
    hasAccentBorder: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SolarDarkGray
        ),
        border = if (hasAccentBorder) {
            BorderStroke(1.dp, SolarYellow.copy(alpha = 0.3f))
        } else {
            BorderStroke(1.dp, SolarLightGray.copy(alpha = 0.3f))
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}
