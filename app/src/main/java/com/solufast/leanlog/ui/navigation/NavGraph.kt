package com.solufast.leanlog.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.solufast.leanlog.ui.dashboard.DashboardScreen
import com.solufast.leanlog.ui.history.WorkoutHistoryScreen
import com.solufast.leanlog.ui.metrics.BodyMetricsScreen
import com.solufast.leanlog.ui.reports.ReportsScreen
import com.solufast.leanlog.ui.workout.AddWorkoutScreen
import com.solufast.leanlog.ui.workout.AddWorkoutViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Add : Screen("add", "Add", Icons.Default.Add)
    object History : Screen("history", "History", Icons.Default.FitnessCenter)
    object Metrics : Screen("metrics", "Metrics", Icons.Default.MonitorWeight)
    object Reports : Screen("reports", "Reports", Icons.Default.BarChart)
}

// Edit route keeps its own path so History can push it without losing its back-stack entry.
private const val EDIT_ROUTE_BASE = "edit_workout"
fun editWorkoutRoute(sessionId: Long) = "$EDIT_ROUTE_BASE/$sessionId"

private val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Add,
    Screen.History,
    Screen.Metrics,
    Screen.Reports
)

@Composable
fun LeanLogNavGraph() {
    val navController = rememberNavController()

    androidx.compose.material3.Scaffold(
        bottomBar = { LeanLogBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen() }

            // New workout
            composable(Screen.Add.route) {
                val vm: AddWorkoutViewModel = viewModel()
                AddWorkoutScreen(
                    vm = vm,
                    onSessionSaved = {
                        navController.navigate(Screen.History.route) {
                            popUpTo(Screen.Add.route) { inclusive = true }
                        }
                    }
                )
            }

            // Edit existing workout — separate route so History stays in back-stack
            composable(
                route = "$EDIT_ROUTE_BASE/{sessionId}",
                arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
            ) { backStackEntry ->
                val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: return@composable
                val vm: AddWorkoutViewModel = viewModel()
                vm.loadSessionForEdit(sessionId)
                AddWorkoutScreen(
                    vm = vm,
                    onSessionSaved = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.History.route) {
                WorkoutHistoryScreen(
                    onEditSession = { sessionId ->
                        navController.navigate(editWorkoutRoute(sessionId))
                    }
                )
            }
            composable(Screen.Metrics.route) { BodyMetricsScreen() }
            composable(Screen.Reports.route) { ReportsScreen() }
        }
    }
}

@Composable
private fun LeanLogBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
