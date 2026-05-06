package com.example.surya_shaktisolarmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.surya_shaktisolarmonitor.ui.components.BottomNavBar
import com.example.surya_shaktisolarmonitor.ui.navigation.AppNavigation
import com.example.surya_shaktisolarmonitor.ui.theme.SuryaShaktiSolarMonitorTheme
import com.example.surya_shaktisolarmonitor.viewmodel.SolarViewModel

/**
 * MainActivity — Entry point for Surya-Shakti Solar Monitor.
 *
 * Sets up:
 * - Edge-to-edge display
 * - Custom Yellow/Black theme
 * - Navigation with bottom bar
 * - Shared ViewModel across all screens
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuryaShaktiSolarMonitorTheme {
                val navController = rememberNavController()
                val viewModel: SolarViewModel = viewModel()

                // Get the current route for bottom nav highlighting
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    // Pop up to the start destination to avoid building up a
                                    // large stack of destinations on the back stack
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination
                                    launchSingleTop = true
                                    // Restore state when re-selecting a previously selected tab
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}