package com.example.nabtest.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nabtest.LoadingState
import com.example.nabtest.models.Forecast
import com.example.nabtest.models.ForecastCity
import com.example.nabtest.repositories.ForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ForecastRepository) : ViewModel() {
    private val forecastsLiveData = MutableLiveData<List<Forecast>?>()

    val queryString = MutableLiveData<String>()

    val loadingState = MutableLiveData<LoadingState>()

    val errorInputText = MutableLiveData<String?>()

    val errorText = MutableLiveData<String>()

    private var currentQueryString: String? = null

    private val compositeDisposable = CompositeDisposable()

    fun getForecasts() = forecastsLiveData

    fun search(isRefresh: Boolean = false) {
        errorInputText.value = null
        val qString = if (isRefresh) currentQueryString else queryString.value

        if (qString.isNullOrEmpty() || qString.length < 3) {
            if (!isRefresh)
                errorInputText.value = "Search term length must be at least 3 characters."
            loadingState.value = LoadingState.ERROR
            return
        }

        loadingState.value = if (isRefresh) LoadingState.REFRESHING else LoadingState.LOADING

        compositeDisposable.add(repository.getForecasts(qString.lowercase(), isRefresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                forecastsLiveData.value = it.forecasts
                loadingState.value = LoadingState.SUCCESS
            }, {
                it.printStackTrace()
                loadingState.value = LoadingState.ERROR
                errorText.value = "Error loading data."
            })
        )
    }

    public override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}