package com.fithome.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val iconKey: String,
    val enabledToday: Boolean = true,
    val intensity: Int = 20,
    val targetMinutes: Int = 45,
    val minutesToday: Int = 0,
    val exercisesCount: Int = 4
)
