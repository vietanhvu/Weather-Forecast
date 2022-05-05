package com.example.nabtest.models

data class Forecast(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temp,
    val weather: List<Weather>,
    val pressure: Float,
    val humidity: Float,
    val speed: Float,
    val deg: Float,
    val gust: Float,
    val clouds: Float,
    val pop: Float,
    val rain: Float
)