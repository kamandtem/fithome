package com.fithome.app.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fithome.app.data.model.Session
import com.fithome.app.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DayKcal(val dayName: String, val kcal: Int, val isToday: Boolean)

data class HistoryUiState(
    val sessions: List<Session> = emptyList(),
    val totalKcal: Int = 0,
    val totalMinutes: Int = 0,
    val weeklyData: List<DayKcal> = listOf(
        DayKcal("Mon", 420, false),
        DayKcal("Tue", 580, false),
        DayKcal("Wed", 320, false),
        DayKcal("Thu", 680, false),
        DayKcal("Fri", 540, true),
        DayKcal("Sat", 720, false),
        DayKcal("Sun", 450, false)
    )
)

class HistoryViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            workoutRepository.getAllSessions().collect { list ->
                val totalKcal = list.sumOf { it.kcal }
                val totalMin = list.sumOf { it.durationSec } / 60
                _uiState.update {
                    it.copy(
                        sessions = list,
                        totalKcal = if (totalKcal > 0) totalKcal else 1220,
                        totalMinutes = if (totalMin > 0) totalMin else 105
                    )
                }
            }
        }
    }
}
