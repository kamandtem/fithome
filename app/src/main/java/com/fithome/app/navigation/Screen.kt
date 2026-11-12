package com.fithome.app.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object WorkoutSession : Screen("workout_session/{workoutId}") {
        fun createRoute(workoutId: Long): String = "workout_session/$workoutId"
    }
    data object History : Screen("history")
    data object Settings : Screen("settings")
    data object Profile : Screen("profile")
    data object Notifications : Screen("notifications")
}
