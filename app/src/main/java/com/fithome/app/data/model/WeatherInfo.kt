package com.fithome.app.data.model

data class WeatherInfo(
    val isToday: Boolean,
    val temperature: Int,
    val lowTemperature: Int,
    val description: String,
    val sensibleTemperature: Int,
    val humidityPercent: Int,
    val pressureHpa: Int
)
