package com.fithome.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fithome.app.data.local.UserPreferencesRepository
import com.fithome.app.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val weightKg: Float = 75f,
    val isMetric: Boolean = true,
    val remindersEnabled: Boolean = true,
    val hydrationGoalMl: Int = 2500,
    val showClearDialog: Boolean = false
)

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val weight = preferencesRepository.userWeightKg.first()
            val metric = preferencesRepository.metricUnits.first()
            val reminders = preferencesRepository.remindersEnabled.first()
            val hydrationGoal = preferencesRepository.hydrationGoalMl.first()

            _uiState.update {
                it.copy(
                    weightKg = weight,
                    isMetric = metric,
                    remindersEnabled = reminders,
                    hydrationGoalMl = hydrationGoal
                )
            }
        }
    }

    fun setWeightKg(weight: Float) {
        viewModelScope.launch {
            preferencesRepository.setUserWeightKg(weight)
            _uiState.update { it.copy(weightKg = weight) }
        }
    }

    fun toggleMetricUnits() {
        val next = !_uiState.value.isMetric
        viewModelScope.launch {
            preferencesRepository.setMetricUnits(next)
            _uiState.update { it.copy(isMetric = next) }
        }
    }

    fun toggleReminders() {
        val next = !_uiState.value.remindersEnabled
        viewModelScope.launch {
            preferencesRepository.setRemindersEnabled(next)
            _uiState.update { it.copy(remindersEnabled = next) }
        }
    }

    fun setHydrationGoal(ml: Int) {
        viewModelScope.launch {
            preferencesRepository.setHydrationGoal(ml)
            _uiState.update { it.copy(hydrationGoalMl = ml) }
        }
    }

    fun showClearDialog() {
        _uiState.update { it.copy(showClearDialog = true) }
    }

    fun dismissClearDialog() {
        _uiState.update { it.copy(showClearDialog = false) }
    }

    fun clearAllData() {
        viewModelScope.launch {
            workoutRepository.clearAllData()
            preferencesRepository.clearUserData()
            _uiState.update { it.copy(showClearDialog = false) }
        }
    }
}
