package com.fithome.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fithome.app.navigation.Screen
import com.fithome.app.ui.screens.dashboard.DashboardScreen
import com.fithome.app.ui.screens.dashboard.DashboardViewModel
import com.fithome.app.ui.screens.history.HistoryScreen
import com.fithome.app.ui.screens.history.HistoryViewModel
import com.fithome.app.ui.screens.notifications.NotificationsScreen
import com.fithome.app.ui.screens.notifications.NotificationsViewModel
import com.fithome.app.ui.screens.profile.ProfileScreen
import com.fithome.app.ui.screens.profile.ProfileViewModel
import com.fithome.app.ui.screens.session.WorkoutSessionScreen
import com.fithome.app.ui.screens.session.WorkoutSessionViewModel
import com.fithome.app.ui.screens.settings.SettingsScreen
import com.fithome.app.ui.screens.settings.SettingsViewModel
import com.fithome.app.ui.theme.FitHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // Light-only app: always use dark status/navigation icons so they stay visible
        // on the light background, even when the phone is in system dark mode.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            FitHomeTheme {
                FitHomeNavHost()
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> viewModelFactory(crossinline initializer: () -> VM): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = initializer() as T
    }

@Composable
fun FitHomeNavHost() {
    val navController = rememberNavController()
    val app = FitHomeApplication.instance

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        enterTransition = {
            fadeIn(animationSpec = tween(280)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(280)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(280)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(280)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(280)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(280)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(280)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(280)
            )
        }
    ) {
        // 1. Dashboard Screen
        composable(Screen.Dashboard.route) {
            val viewModel: DashboardViewModel = viewModel(
                factory = viewModelFactory {
                    DashboardViewModel(
                        workoutRepository = app.workoutRepository,
                        weatherRepository = app.weatherRepository,
                        preferencesRepository = app.userPreferencesRepository
                    )
                }
            )

            DashboardScreen(
                viewModel = viewModel,
                onWorkoutClick = { workoutId ->
                    navController.navigate(Screen.WorkoutSession.createRoute(workoutId))
                },
                onQuickStartClick = {
                    navController.navigate(Screen.WorkoutSession.createRoute(1L))
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onNotificationClick = {
                    navController.navigate(Screen.Notifications.route)
                }
            )
        }

        // 2. Workout Session Screen (Screen 1)
        composable(
            route = Screen.WorkoutSession.route,
            arguments = listOf(
                navArgument("workoutId") {
                    type = NavType.StringType
                    defaultValue = "1"
                }
            )
        ) { backStackEntry ->
            val workoutIdStr = backStackEntry.arguments?.getString("workoutId") ?: "1"
            val savedStateHandle = SavedStateHandle(mapOf("workoutId" to workoutIdStr))

            val viewModel: WorkoutSessionViewModel = viewModel(
                factory = viewModelFactory {
                    WorkoutSessionViewModel(
                        savedStateHandle = savedStateHandle,
                        workoutRepository = app.workoutRepository,
                        preferencesRepository = app.userPreferencesRepository
                    )
                }
            )

            WorkoutSessionScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { navController.navigate(Screen.Notifications.route) }
            )
        }

        // 3. History Screen
        composable(Screen.History.route) {
            val viewModel: HistoryViewModel = viewModel(
                factory = viewModelFactory {
                    HistoryViewModel(workoutRepository = app.workoutRepository)
                }
            )

            HistoryScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onQuickStartClick = {
                    navController.navigate(Screen.WorkoutSession.createRoute(1L))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onNotificationClick = {
                    navController.navigate(Screen.Notifications.route)
                }
            )
        }

        // 4. Settings Screen
        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(
                factory = viewModelFactory {
                    SettingsViewModel(
                        preferencesRepository = app.userPreferencesRepository,
                        workoutRepository = app.workoutRepository
                    )
                }
            )

            SettingsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onHistoryClick = { navController.navigate(Screen.History.route) },
                onQuickStartClick = { navController.navigate(Screen.WorkoutSession.createRoute(1L)) },
                onNotificationClick = { navController.navigate(Screen.Notifications.route) }
            )
        }

        // 5. Profile Screen
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = viewModel(
                factory = viewModelFactory {
                    ProfileViewModel(
                        preferencesRepository = app.userPreferencesRepository
                    )
                }
            )

            ProfileScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { navController.navigate(Screen.Notifications.route) }
            )
        }

        // 6. Notifications Screen
        composable(Screen.Notifications.route) {
            val viewModel: NotificationsViewModel = viewModel(
                factory = viewModelFactory {
                    NotificationsViewModel(
                        preferencesRepository = app.userPreferencesRepository
                    )
                }
            )

            NotificationsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
