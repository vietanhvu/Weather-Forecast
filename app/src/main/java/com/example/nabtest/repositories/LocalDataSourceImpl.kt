package com.example.nabtest.repositories

import android.content.SharedPreferences
import com.example.nabtest.models.ForecastCity
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalDataSource {
    override fun getForecasts(city: String): Maybe<ForecastCity> {
        val cachedString = sharedPreferences.getString(city, "")
        return if (cachedString.isNullOrEmpty()) Maybe.empty() else
            Maybe.just(
                Gson().fromJson(
                    cachedString,
                    ForecastCity::class.java
                )
            )
    }

    override fun saveForecasts(city: String, forecastCity: ForecastCity): Completable =
        Completable.fromAction {
            sharedPreferences.edit().putString(city, Gson().toJson(forecastCity)).commit()
        }
}