package com.example.nabtest.repositories

import com.example.nabtest.api.ApiService
import com.example.nabtest.models.ForecastCity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) : RemoteDataSource {
    override fun getForecasts(city: String): Single<ForecastCity> = apiService.getForecasts(city)
}