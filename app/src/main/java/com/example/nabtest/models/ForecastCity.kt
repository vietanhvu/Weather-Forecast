package com.example.nabtest.models

import com.google.gson.annotations.SerializedName

data class ForecastCity(
    val city: City,
    val cod: String,
    val message: Float,
    @SerializedName("list") val forecasts: List<Forecast>
)