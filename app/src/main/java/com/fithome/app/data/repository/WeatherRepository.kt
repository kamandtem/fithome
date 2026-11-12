package com.fithome.app.data.repository

import com.fithome.app.data.model.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface WeatherRepository {
    fun getTodayWeather(): Flow<WeatherInfo>
    fun getTomorrowWeather(): Flow<WeatherInfo>
}

class MockWeatherRepository : WeatherRepository {
    private val today = WeatherInfo(
        isToday = true,
        temperature = 23,
        lowTemperature = 13,
        description = "A sun with a cool breeze",
        sensibleTemperature = 25,
        humidityPercent = 63,
        pressureHpa = 1009
    )

    private val tomorrow = WeatherInfo(
        isToday = false,
        temperature = 21,
        lowTemperature = 12,
        description = "Partly cloudy with light breeze",
        sensibleTemperature = 22,
        humidityPercent = 58,
        pressureHpa = 1012
    )

    override fun getTodayWeather(): Flow<WeatherInfo> = flowOf(today)

    override fun getTomorrowWeather(): Flow<WeatherInfo> = flowOf(tomorrow)
}
