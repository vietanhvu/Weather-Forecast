package com.example.nabtest.repositories

import com.example.nabtest.models.ForecastCity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface LocalDataSource {
    fun getForecasts(city: String): Maybe<ForecastCity>

    fun saveForecasts(city: String, forecastCity: ForecastCity): Completable
}