package com.fithome.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutId: Long,
    val workoutName: String,
    val startedAt: Long,
    val durationSec: Int,
    val intensity: Int,
    val kcal: Int
)
