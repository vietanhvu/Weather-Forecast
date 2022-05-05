package com.example.nabtest.models

import com.google.gson.annotations.SerializedName

data class ForecastCity(
    val city: City? = null,
    val cod: String? = null,
    val message: Float? = null,
    @SerializedName("list") val forecasts: List<Forecast>? = null
)