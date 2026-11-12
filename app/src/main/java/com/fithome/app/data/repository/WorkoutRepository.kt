package com.fithome.app.data.repository

import com.fithome.app.data.local.SessionDao
import com.fithome.app.data.local.WorkoutDao
import com.fithome.app.data.model.Session
import com.fithome.app.data.model.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val sessionDao: SessionDao
) {
    fun getAllWorkouts(): Flow<List<Workout>> = workoutDao.getAllWorkouts()

    fun getWorkoutById(id: Long): Flow<Workout?> = workoutDao.getWorkoutById(id)

    suspend fun toggleWorkoutEnabled(id: Long, enabled: Boolean) {
        workoutDao.updateEnabled(id, enabled)
    }

    suspend fun updateWorkoutSettings(id: Long, intensity: Int, targetMinutes: Int) {
        workoutDao.updateIntensityAndTarget(id, intensity, targetMinutes)
    }

    suspend fun saveSession(session: Session) {
        sessionDao.insert(session)
        val minutes = (session.durationSec / 60).coerceAtLeast(1)
        workoutDao.addMinutes(session.workoutId, minutes)
    }

    fun getAllSessions(): Flow<List<Session>> = sessionDao.getAllSessions()

    fun getTodayKcal(): Flow<Int> {
        val startOfDay = getStartOfDayMillis()
        return sessionDao.getTotalKcalToday(startOfDay).map { it ?: 0 }
    }

    fun getTodayWorkoutCount(): Flow<Int> {
        val startOfDay = getStartOfDayMillis()
        return sessionDao.getWorkoutCountToday(startOfDay)
    }

    suspend fun addWorkout(workout: Workout): Long = workoutDao.insert(workout)

    suspend fun deleteWorkout(workout: Workout) = workoutDao.delete(workout)

    suspend fun clearAllData() {
        workoutDao.clearAll()
        sessionDao.clearAll()
    }

    private fun getStartOfDayMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
