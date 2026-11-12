package com.fithome.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fithome.app.data.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY startedAt DESC")
    fun getAllSessions(): Flow<List<Session>>

    @Query("SELECT * FROM sessions WHERE workoutId = :workoutId ORDER BY startedAt DESC")
    fun getSessionsForWorkout(workoutId: Long): Flow<List<Session>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: Session): Long

    @Query("SELECT SUM(kcal) FROM sessions WHERE startedAt >= :startOfDayMs")
    fun getTotalKcalToday(startOfDayMs: Long): Flow<Int?>

    @Query("SELECT COUNT(*) FROM sessions WHERE startedAt >= :startOfDayMs")
    fun getWorkoutCountToday(startOfDayMs: Long): Flow<Int>

    @Query("DELETE FROM sessions")
    suspend fun clearAll()
}
