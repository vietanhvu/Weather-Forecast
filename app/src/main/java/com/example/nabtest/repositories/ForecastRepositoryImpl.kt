package com.example.nabtest.repositories

import com.example.nabtest.models.ForecastCity
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ForecastRepository {

    override fun getForecasts(city: String, isRefresh: Boolean): Single<ForecastCity> {
        return if (isRefresh) getForecastFromRemoteThenSaveToLocal(city) else localDataSource.getForecasts(
            city
        ).onErrorResumeNext { Maybe.empty() }
            .switchIfEmpty(getForecastFromRemoteThenSaveToLocal(city))
    }

    private fun getForecastFromRemoteThenSaveToLocal(city: String): Single<ForecastCity> =
        remoteDataSource.getForecasts(city).doOnSuccess {
            localDataSource.saveForecasts(city, it).subscribeOn(Schedulers.io()).subscribe()
        }
}