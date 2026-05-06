package com.example.surya_shaktisolarmonitor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.surya_shaktisolarmonitor.ui.components.BottomNavItem
import com.example.surya_shaktisolarmonitor.ui.screens.ConsumptionScreen
import com.example.surya_shaktisolarmonitor.ui.screens.DashboardScreen
import com.example.surya_shaktisolarmonitor.ui.screens.GenerationLogScreen
import com.example.surya_shaktisolarmonitor.ui.screens.ReportsScreen
import com.example.surya_shaktisolarmonitor.ui.screens.SavingsScreen
import com.example.surya_shaktisolarmonitor.viewmodel.SolarViewModel

/**
 * App Navigation Graph — Defines all screen routes and navigation flow.
 *
 * Uses Jetpack Compose Navigation with a bottom navigation bar.
 * All screens share the same SolarViewModel instance for data consistency.
 *
 * Routes:
 * - dashboard: Main overview with stats and circular progress
 * - generation: Log daily solar generation
 * - consumption: Track meter readings and consumption
 * - savings: Calculate and view electricity savings
 * - reports: 30-day history and detailed reports
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: SolarViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Dashboard.route,
        modifier = modifier
    ) {
        // Dashboard — Main overview screen
        composable(BottomNavItem.Dashboard.route) {
            DashboardScreen(viewModel = viewModel)
        }

        // Generation Log — Enter daily solar generation
        composable(BottomNavItem.Generation.route) {
            GenerationLogScreen(viewModel = viewModel)
        }

        // Consumption Tracker — Meter readings and usage
        composable(BottomNavItem.Consumption.route) {
            ConsumptionScreen(viewModel = viewModel)
        }

        // Savings Calculator — Financial savings tracking
        composable(BottomNavItem.Savings.route) {
            SavingsScreen(viewModel = viewModel)
        }

        // Reports — 30-day history and charts
        composable(BottomNavItem.Reports.route) {
            ReportsScreen(viewModel = viewModel)
        }
    }
}
