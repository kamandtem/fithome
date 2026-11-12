package com.fithome.app.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fithome.app.data.local.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isUnread: Boolean
)

data class NotificationsUiState(
    val items: List<NotificationItem> = listOf(
        NotificationItem(1, "Goal Reached! \uD83C\uDFC6", "You crushed your daily active calorie target.", "10m ago", true),
        NotificationItem(2, "Hydration Reminder \uD83D\uDCA7", "Don't forget to drink 250ml water after your run.", "1h ago", true),
        NotificationItem(3, "Weekly Insight \uD83D\uDCC8", "Your strength consistency is up 18% this week.", "Yesterday", false),
        NotificationItem(4, "New Personal Best \u26A1", "Fastest 5K pace recorded during morning cardio.", "2d ago", false)
    )
)

class NotificationsViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    fun markAllRead() {
        viewModelScope.launch {
            preferencesRepository.setNotificationsUnread(false)
            _uiState.update { state ->
                state.copy(items = state.items.map { it.copy(isUnread = false) })
            }
        }
    }
}
