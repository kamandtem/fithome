package com.fithome.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fithome.app.data.model.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY id ASC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workouts WHERE id = :id LIMIT 1")
    fun getWorkoutById(id: Long): Flow<Workout?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<Workout>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout): Long

    @Update
    suspend fun update(workout: Workout)

    @Query("UPDATE workouts SET enabledToday = :enabled WHERE id = :id")
    suspend fun updateEnabled(id: Long, enabled: Boolean)

    @Query("UPDATE workouts SET intensity = :intensity, targetMinutes = :targetMinutes WHERE id = :id")
    suspend fun updateIntensityAndTarget(id: Long, intensity: Int, targetMinutes: Int)

    @Query("UPDATE workouts SET minutesToday = minutesToday + :minutes WHERE id = :id")
    suspend fun addMinutes(id: Long, minutes: Int)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("DELETE FROM workouts")
    suspend fun clearAll()
}
