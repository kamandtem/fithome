package com.fithome.app.ui.screens.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fithome.app.data.local.UserPreferencesRepository
import com.fithome.app.data.model.Session
import com.fithome.app.data.model.Workout
import com.fithome.app.data.repository.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class TabState(
    val category: String,
    val title: String,
    val subtitle: String,
    var intensity: Int = 20,
    var durationMinutes: Int = 45
)

enum class TimerState {
    IDLE, RUNNING, PAUSED
}

data class SessionUiState(
    val selectedCategory: String = "Cardio",
    val tabs: List<TabState> = listOf(
        TabState("Cardio", "Cardio", "x4 Exercises", 18, 46),
        TabState("Strength", "Strength", "x6 Exercises", 20, 45),
        TabState("Yoga", "Yoga", "x3 Exercises", 14, 30)
    ),
    val currentIntensity: Int = 20,
    val currentDurationMinutes: Int = 46,
    val isAutoIntensity: Boolean = false,
    val timerState: TimerState = TimerState.IDLE,
    val remainingSeconds: Int = 46 * 60,
    val elapsedSeconds: Int = 0,
    val userWeightKg: Float = 75.0f,
    val hasUnreadNotifications: Boolean = true,
    val showSummarySheet: Boolean = false,
    val showTipsSheet: Boolean = false,
    val showStepperDialog: Boolean = false,
    val hydrationToastVisible: Boolean = false,
    val finishedSession: Session? = null
) {
    val estimatedKcal: Int
        get() {
            val met = 2.0f + (currentIntensity / 30f) * 10f
            val hours = currentDurationMinutes / 60f
            return (met * userWeightKg * hours).roundToInt()
        }

    val burnedKcalSoFar: Int
        get() {
            val met = 2.0f + (currentIntensity / 30f) * 10f
            val hours = elapsedSeconds / 3600f
            return (met * userWeightKg * hours).roundToInt().coerceAtLeast(0)
        }
}

class WorkoutSessionViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val initialWorkoutId: Long = savedStateHandle.get<String>("workoutId")?.toLongOrNull() ?: 1L

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var initialWorkoutName: String = "Running"

    init {
        viewModelScope.launch {
            val weight = preferencesRepository.userWeightKg.first()
            val unread = preferencesRepository.notificationsUnread.first()
            _uiState.update { it.copy(userWeightKg = weight, hasUnreadNotifications = unread) }

            val workout = workoutRepository.getWorkoutById(initialWorkoutId).first()
            if (workout != null) {
                initialWorkoutName = workout.name
                val category = workout.category
                selectTab(category, workout.intensity, workout.targetMinutes)
            }
        }
    }

    fun selectTab(category: String, defaultIntensity: Int? = null, defaultDuration: Int? = null) {
        _uiState.update { state ->
            val tab = state.tabs.find { it.category == category } ?: state.tabs.first()
            val intensity = defaultIntensity ?: tab.intensity
            val duration = defaultDuration ?: tab.durationMinutes
            tab.intensity = intensity
            tab.durationMinutes = duration

            state.copy(
                selectedCategory = category,
                currentIntensity = intensity,
                currentDurationMinutes = duration,
                remainingSeconds = if (state.timerState == TimerState.IDLE) duration * 60 else state.remainingSeconds
            )
        }
    }

    fun setIntensity(newIntensity: Int) {
        _uiState.update { state ->
            state.tabs.find { it.category == state.selectedCategory }?.intensity = newIntensity
            state.copy(currentIntensity = newIntensity)
        }
    }

    fun setDuration(newMinutes: Int) {
        _uiState.update { state ->
            state.tabs.find { it.category == state.selectedCategory }?.durationMinutes = newMinutes
            state.copy(
                currentDurationMinutes = newMinutes,
                remainingSeconds = if (state.timerState == TimerState.IDLE) newMinutes * 60 else state.remainingSeconds,
                showStepperDialog = false
            )
        }
    }

    fun toggleAutoIntensity() {
        _uiState.update { it.copy(isAutoIntensity = !it.isAutoIntensity) }
    }

    fun openStepperDialog() {
        _uiState.update { it.copy(showStepperDialog = true) }
    }

    fun dismissStepperDialog() {
        _uiState.update { it.copy(showStepperDialog = false) }
    }

    fun openTipsSheet() {
        _uiState.update { it.copy(showTipsSheet = true) }
    }

    fun dismissTipsSheet() {
        _uiState.update { it.copy(showTipsSheet = false) }
    }

    fun dismissSummarySheet() {
        _uiState.update { it.copy(showSummarySheet = false) }
    }

    fun togglePlayPause() {
        when (_uiState.value.timerState) {
            TimerState.IDLE, TimerState.PAUSED -> startTimer()
            TimerState.RUNNING -> pauseTimer()
        }
    }

    private fun startTimer() {
        _uiState.update { it.copy(timerState = TimerState.RUNNING) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val current = _uiState.value
                if (current.remainingSeconds > 0) {
                    val nextRemaining = current.remainingSeconds - 1
                    val nextElapsed = current.elapsedSeconds + 1

                    // Auto adaptive intensity (micro-adjust every 60s if Auto is enabled)
                    val nextIntensity = if (current.isAutoIntensity && nextElapsed % 60 == 0) {
                        (current.currentIntensity + (-1..1).random()).coerceIn(10, 30)
                    } else current.currentIntensity

                    _uiState.update {
                        it.copy(
                            remainingSeconds = nextRemaining,
                            elapsedSeconds = nextElapsed,
                            currentIntensity = nextIntensity
                        )
                    }
                } else {
                    finishWorkout()
                    break
                }
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(timerState = TimerState.PAUSED) }
    }

    fun finishWorkout() {
        timerJob?.cancel()
        val current = _uiState.value
        val durationSec = current.elapsedSeconds.coerceAtLeast(60)
        val kcal = current.burnedKcalSoFar.coerceAtLeast(10)

        val session = Session(
            workoutId = initialWorkoutId,
            workoutName = initialWorkoutName,
            startedAt = System.currentTimeMillis() - (durationSec * 1000L),
            durationSec = durationSec,
            intensity = current.currentIntensity,
            kcal = kcal
        )

        viewModelScope.launch {
            workoutRepository.saveSession(session)
            _uiState.update {
                it.copy(
                    timerState = TimerState.IDLE,
                    elapsedSeconds = 0,
                    remainingSeconds = current.currentDurationMinutes * 60,
                    finishedSession = session,
                    showSummarySheet = true
                )
            }
        }
    }

    fun logHydration() {
        viewModelScope.launch {
            preferencesRepository.addHydration(250)
            _uiState.update { it.copy(hydrationToastVisible = true) }
            delay(1800L)
            _uiState.update { it.copy(hydrationToastVisible = false) }
        }
    }
}
