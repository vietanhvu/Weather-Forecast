package com.example.nabtest.repositories

import com.example.nabtest.models.ForecastCity
import io.reactivex.rxjava3.core.Single

interface RemoteDataSource {
    fun getForecasts(city: String): Single<ForecastCity>
}