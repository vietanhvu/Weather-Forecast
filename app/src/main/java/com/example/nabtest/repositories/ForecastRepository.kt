package com.example.nabtest.repositories

import com.example.nabtest.models.ForecastCity
import io.reactivex.rxjava3.core.Single

interface ForecastRepository {
    fun getForecasts(city: String, isRefresh: Boolean): Single<ForecastCity>
}