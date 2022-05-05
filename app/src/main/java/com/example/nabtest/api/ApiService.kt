package com.example.nabtest.api

import com.example.nabtest.BuildConfig
import com.example.nabtest.models.ForecastCity
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast/daily")
    fun getForecasts(
        @Query("q") city: String,
        @Query("cnt") cnt: Int = 7,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = BuildConfig.API_KEY
    ): Single<ForecastCity>
}