package com.fithome.app

import android.app.Application
import com.fithome.app.data.local.FitHomeDatabase
import com.fithome.app.data.local.UserPreferencesRepository
import com.fithome.app.data.repository.MockWeatherRepository
import com.fithome.app.data.repository.WeatherRepository
import com.fithome.app.data.repository.WorkoutRepository

class FitHomeApplication : Application() {

    val database by lazy { FitHomeDatabase.getInstance(this) }
    val workoutRepository by lazy { WorkoutRepository(database.workoutDao(), database.sessionDao()) }
    val weatherRepository: WeatherRepository by lazy { MockWeatherRepository() }
    val userPreferencesRepository by lazy { UserPreferencesRepository(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: FitHomeApplication
            private set
    }
}
