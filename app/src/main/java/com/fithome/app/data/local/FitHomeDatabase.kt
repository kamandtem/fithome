package com.fithome.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fithome.app.data.model.Session
import com.fithome.app.data.model.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Workout::class, Session::class], version = 1, exportSchema = false)
abstract class FitHomeDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: FitHomeDatabase? = null

        fun getInstance(context: Context): FitHomeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitHomeDatabase::class.java,
                    "fithome_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                database.workoutDao().insertAll(
                                    listOf(
                                        Workout(
                                            id = 1,
                                            name = "Running",
                                            category = "Cardio",
                                            iconKey = "cardio",
                                            enabledToday = false,
                                            intensity = 18,
                                            targetMinutes = 46,
                                            minutesToday = 139,
                                            exercisesCount = 4
                                        ),
                                        Workout(
                                            id = 2,
                                            name = "Strength",
                                            category = "Strength",
                                            iconKey = "strength",
                                            enabledToday = true,
                                            intensity = 20,
                                            targetMinutes = 45,
                                            minutesToday = 193,
                                            exercisesCount = 6
                                        ),
                                        Workout(
                                            id = 3,
                                            name = "Yoga",
                                            category = "Yoga",
                                            iconKey = "yoga",
                                            enabledToday = false,
                                            intensity = 14,
                                            targetMinutes = 30,
                                            minutesToday = 45,
                                            exercisesCount = 3
                                        ),
                                        Workout(
                                            id = 4,
                                            name = "Cycling",
                                            category = "Cardio",
                                            iconKey = "cycling",
                                            enabledToday = false,
                                            intensity = 22,
                                            targetMinutes = 60,
                                            minutesToday = 50,
                                            exercisesCount = 4
                                        ),
                                        Workout(
                                            id = 5,
                                            name = "Swimming",
                                            category = "Cardio",
                                            iconKey = "swimming",
                                            enabledToday = false,
                                            intensity = 24,
                                            targetMinutes = 45,
                                            minutesToday = 0,
                                            exercisesCount = 5
                                        ),
                                        Workout(
                                            id = 6,
                                            name = "HIIT",
                                            category = "Strength",
                                            iconKey = "hiit",
                                            enabledToday = false,
                                            intensity = 26,
                                            targetMinutes = 30,
                                            minutesToday = 0,
                                            exercisesCount = 8
                                        )
                                    )
                                )

                                val now = System.currentTimeMillis()
                                val oneHour = 3600 * 1000L
                                database.sessionDao().insert(
                                    Session(
                                        workoutId = 2,
                                        workoutName = "Strength",
                                        startedAt = now - 4 * oneHour,
                                        durationSec = 2700,
                                        intensity = 20,
                                        kcal = 540
                                    )
                                )
                                database.sessionDao().insert(
                                    Session(
                                        workoutId = 1,
                                        workoutName = "Running",
                                        startedAt = now - 8 * oneHour,
                                        durationSec = 3600,
                                        intensity = 18,
                                        kcal = 680
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
