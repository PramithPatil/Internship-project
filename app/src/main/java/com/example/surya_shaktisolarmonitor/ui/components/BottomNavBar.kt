package com.example.surya_shaktisolarmonitor.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.surya_shaktisolarmonitor.ui.theme.SolarBlack
import com.example.surya_shaktisolarmonitor.ui.theme.SolarDarkGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarTextGray
import com.example.surya_shaktisolarmonitor.ui.theme.SolarYellow

/**
 * Navigation destinations for the bottom navigation bar.
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.Rounded.Dashboard)
    data object Generation : BottomNavItem("generation", "Generate", Icons.Rounded.WbSunny)
    data object Consumption : BottomNavItem("consumption", "Consume", Icons.Rounded.ElectricBolt)
    data object Savings : BottomNavItem("savings", "Savings", Icons.Rounded.Savings)
    data object Reports : BottomNavItem("reports", "Reports", Icons.Rounded.Assessment)
}

/**
 * Bottom navigation bar with solar-themed styling.
 *
 * Features:
 * - 5 tabs: Dashboard, Generation, Consumption, Savings, Reports
 * - Yellow accent for selected tab
 * - Dark background matching the app theme
 *
 * @param currentRoute Currently active navigation route
 * @param onNavigate Callback when a tab is selected
 */
@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Generation,
        BottomNavItem.Consumption,
        BottomNavItem.Savings,
        BottomNavItem.Reports
    )

    NavigationBar(
        containerColor = SolarDarkGray,
        contentColor = SolarYellow
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SolarBlack,
                    selectedTextColor = SolarYellow,
                    unselectedIconColor = SolarTextGray,
                    unselectedTextColor = SolarTextGray,
                    indicatorColor = SolarYellow
                )
            )
        }
    }
}
