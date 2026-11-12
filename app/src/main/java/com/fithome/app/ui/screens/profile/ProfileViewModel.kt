package com.fithome.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fithome.app.data.local.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "Vitaliy",
    val weightKg: Float = 75f,
    val heightCm: Int = 180,
    val avatarUri: String? = null,
    val hydrationMl: Int = 1250,
    val isSavedSuccess: Boolean = false
)

class ProfileViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val name = preferencesRepository.userName.first()
            val weight = preferencesRepository.userWeightKg.first()
            val height = preferencesRepository.userHeightCm.first()
            val uri = preferencesRepository.avatarUri.first()
            val hydration = preferencesRepository.hydrationMl.first()

            _uiState.update {
                it.copy(
                    name = name,
                    weightKg = weight,
                    heightCm = height,
                    avatarUri = uri,
                    hydrationMl = hydration
                )
            }
        }
    }

    fun updateProfile(name: String, weight: Float, height: Int) {
        viewModelScope.launch {
            preferencesRepository.setUserName(name)
            preferencesRepository.setUserWeightKg(weight)
            preferencesRepository.setUserHeightCm(height)
            _uiState.update {
                it.copy(
                    name = name,
                    weightKg = weight,
                    heightCm = height,
                    isSavedSuccess = true
                )
            }
        }
    }

    fun setAvatarUri(uri: String?) {
        viewModelScope.launch {
            preferencesRepository.setAvatarUri(uri)
            _uiState.update { it.copy(avatarUri = uri) }
        }
    }

    fun resetSaveSuccess() {
        _uiState.update { it.copy(isSavedSuccess = false) }
    }
}
