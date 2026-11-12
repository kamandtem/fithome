package com.fithome.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fithome.app.data.local.UserPreferencesRepository
import com.fithome.app.data.model.WeatherInfo
import com.fithome.app.data.model.Workout
import com.fithome.app.data.repository.WeatherRepository
import com.fithome.app.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val userName: String = "Vitaliy",
    val avatarUri: String? = null,
    val hasUnreadNotifications: Boolean = true,
    val isWeatherTodaySelected: Boolean = true,
    val weatherInfo: WeatherInfo? = null,
    val workouts: List<Workout> = emptyList(),
    val todayKcal: Int = 1520,
    val todayWorkoutsCount: Int = 2,
    val showWeatherDetails: Boolean = false,
    val showAddWorkoutDialog: Boolean = false,
    val showMenuDropdown: Boolean = false
)

class DashboardViewModel(
    private val workoutRepository: WorkoutRepository,
    private val weatherRepository: WeatherRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Collect user profile preferences
        viewModelScope.launch {
            preferencesRepository.userName.collect { name ->
                _uiState.update { it.copy(userName = name) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.avatarUri.collect { uri ->
                _uiState.update { it.copy(avatarUri = uri) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.notificationsUnread.collect { unread ->
                _uiState.update { it.copy(hasUnreadNotifications = unread) }
            }
        }

        // Collect workouts from Room
        viewModelScope.launch {
            workoutRepository.getAllWorkouts().collect { list ->
                _uiState.update { it.copy(workouts = list) }
            }
        }

        // Collect today's kcal and count
        viewModelScope.launch {
            combine(
                workoutRepository.getTodayKcal(),
                workoutRepository.getTodayWorkoutCount()
            ) { kcal, count ->
                Pair(if (kcal > 0) kcal else 1520, if (count > 0) count else 2)
            }.collect { (kcal, count) ->
                _uiState.update { it.copy(todayKcal = kcal, todayWorkoutsCount = count) }
            }
        }

        // Load today's weather
        loadWeather(isToday = true)
    }

    fun toggleWeatherTab() {
        val nextIsToday = !_uiState.value.isWeatherTodaySelected
        _uiState.update { it.copy(isWeatherTodaySelected = nextIsToday) }
        loadWeather(nextIsToday)
    }

    private fun loadWeather(isToday: Boolean) {
        viewModelScope.launch {
            val weather = if (isToday) {
                weatherRepository.getTodayWeather().first()
            } else {
                weatherRepository.getTomorrowWeather().first()
            }
            _uiState.update { it.copy(weatherInfo = weather) }
        }
    }

    fun toggleWorkout(id: Long, enabled: Boolean) {
        viewModelScope.launch {
            workoutRepository.toggleWorkoutEnabled(id, enabled)
        }
    }

    fun setAvatarUri(uri: String?) {
        viewModelScope.launch {
            preferencesRepository.setAvatarUri(uri)
        }
    }

    fun openWeatherDetails() {
        _uiState.update { it.copy(showWeatherDetails = true) }
    }

    fun dismissWeatherDetails() {
        _uiState.update { it.copy(showWeatherDetails = false) }
    }

    fun openAddWorkoutDialog() {
        _uiState.update { it.copy(showAddWorkoutDialog = true, showMenuDropdown = false) }
    }

    fun dismissAddWorkoutDialog() {
        _uiState.update { it.copy(showAddWorkoutDialog = false) }
    }

    fun toggleMenuDropdown() {
        _uiState.update { it.copy(showMenuDropdown = !it.showMenuDropdown) }
    }

    fun addCustomWorkout(name: String, category: String, durationMin: Int) {
        viewModelScope.launch {
            val newWorkout = Workout(
                name = name,
                category = category,
                iconKey = category.lowercase(),
                enabledToday = true,
                intensity = 20,
                targetMinutes = durationMin,
                minutesToday = 0,
                exercisesCount = 4
            )
            workoutRepository.addWorkout(newWorkout)
            _uiState.update { it.copy(showAddWorkoutDialog = false) }
        }
    }

    fun sortWorkouts(byName: Boolean) {
        _uiState.update { state ->
            val sorted = if (byName) {
                state.workouts.sortedBy { it.name }
            } else {
                state.workouts.sortedByDescending { it.minutesToday }
            }
            state.copy(workouts = sorted, showMenuDropdown = false)
        }
    }
}
