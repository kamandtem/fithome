package com.fithome.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_WEIGHT_KG = floatPreferencesKey("user_weight_kg")
        val USER_HEIGHT_CM = intPreferencesKey("user_height_cm")
        val AVATAR_URI = stringPreferencesKey("avatar_uri")
        val HYDRATION_ML = intPreferencesKey("hydration_ml")
        val HYDRATION_GOAL_ML = intPreferencesKey("hydration_goal_ml")
        val NOTIFICATIONS_UNREAD = booleanPreferencesKey("notifications_unread")
        val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
        val METRIC_UNITS = booleanPreferencesKey("metric_units")
    }

    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME] ?: "Vitaliy"
    }

    val userWeightKg: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_WEIGHT_KG] ?: 75.0f
    }

    val userHeightCm: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_HEIGHT_CM] ?: 180
    }

    val avatarUri: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AVATAR_URI]?.takeIf { it.isNotBlank() }
    }

    val hydrationMl: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.HYDRATION_ML] ?: 1250
    }

    val hydrationGoalMl: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.HYDRATION_GOAL_ML] ?: 2500
    }

    val notificationsUnread: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATIONS_UNREAD] ?: true
    }

    val remindersEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.REMINDERS_ENABLED] ?: true
    }

    val metricUnits: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.METRIC_UNITS] ?: true
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { it[PreferencesKeys.USER_NAME] = name }
    }

    suspend fun setUserWeightKg(weight: Float) {
        context.dataStore.edit { it[PreferencesKeys.USER_WEIGHT_KG] = weight }
    }

    suspend fun setUserHeightCm(height: Int) {
        context.dataStore.edit { it[PreferencesKeys.USER_HEIGHT_CM] = height }
    }

    suspend fun setAvatarUri(uri: String?) {
        context.dataStore.edit { it[PreferencesKeys.AVATAR_URI] = uri ?: "" }
    }

    suspend fun addHydration(amountMl: Int) {
        context.dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.HYDRATION_ML] ?: 1250
            preferences[PreferencesKeys.HYDRATION_ML] = (current + amountMl).coerceAtLeast(0)
        }
    }

    suspend fun setHydrationGoal(goalMl: Int) {
        context.dataStore.edit { it[PreferencesKeys.HYDRATION_GOAL_ML] = goalMl }
    }

    suspend fun setNotificationsUnread(unread: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.NOTIFICATIONS_UNREAD] = unread }
    }

    suspend fun setRemindersEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.REMINDERS_ENABLED] = enabled }
    }

    suspend fun setMetricUnits(metric: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.METRIC_UNITS] = metric }
    }

    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
