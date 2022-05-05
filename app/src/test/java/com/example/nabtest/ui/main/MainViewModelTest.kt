package com.example.nabtest.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.nabtest.LoadingState
import com.example.nabtest.models.ForecastCity
import com.example.nabtest.repositories.ForecastRepository
import com.example.nabtest.utils.SchedulerProvider
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel

    @Mock
    private lateinit var repository: ForecastRepository

    @Mock
    private lateinit var observer: Observer<LoadingState>

    @Mock
    private lateinit var schedulerProvider: SchedulerProvider

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mainViewModel = MainViewModel(repository, schedulerProvider)
        mainViewModel.loadingState.observeForever(observer)
    }

    @Test
    fun testApiFetchDataSuccess() {
        // Mock API response
        mainViewModel.queryString.value = "hanoi"
        `when`(schedulerProvider.io).thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.ui).thenReturn(Schedulers.trampoline())

        `when`(repository.getForecasts("hanoi", false)).thenReturn(Single.just(ForecastCity()))
        mainViewModel.search()
        verify(observer).onChanged(LoadingState.LOADING)
        verify(observer).onChanged(LoadingState.SUCCESS)
    }

    @Test
    fun testApiFetchDataUpperCaseSuccess() {
        // Mock API response
        mainViewModel.queryString.value = "HaNoi"
        `when`(schedulerProvider.io).thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.ui).thenReturn(Schedulers.trampoline())

        `when`(repository.getForecasts("hanoi", false)).thenReturn(Single.just(ForecastCity()))
        mainViewModel.search()
        verify(observer).onChanged(LoadingState.LOADING)
        verify(observer).onChanged(LoadingState.SUCCESS)
    }

    @Test
    fun testApiFetchDataRefreshCaseSuccess() {
        // Mock API response
        // Call api for the first time
        mainViewModel.queryString.value = "HaNoi"
        `when`(schedulerProvider.io).thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.ui).thenReturn(Schedulers.trampoline())

        `when`(repository.getForecasts("hanoi", false)).thenReturn(Single.just(ForecastCity()))
        mainViewModel.search(false)

        // Now, pull to refresh it
        Mockito.reset(observer)
        `when`(repository.getForecasts("hanoi", true)).thenReturn(Single.just(ForecastCity()))
        mainViewModel.search(true)
        verify(observer).onChanged(LoadingState.REFRESHING)
        verify(observer).onChanged(LoadingState.SUCCESS)
    }

    @Test
    fun testApiFetchInputError() {
        mainViewModel.queryString.value = "ha"

        mainViewModel.search(false)
        assert(mainViewModel.errorInputText.value == "Search term length must be at least 3 characters.")
        verify(observer).onChanged(LoadingState.ERROR)
    }

    @Test
    fun testApiFetchRefreshInputError() {
        mainViewModel.queryString.value = "ha"

        mainViewModel.search(false)
        assert(mainViewModel.errorInputText.value == "Search term length must be at least 3 characters.")
        verify(observer).onChanged(LoadingState.ERROR)

        Mockito.reset(observer)
        mainViewModel.search(true)
        verify(observer).onChanged(LoadingState.ERROR)
    }

    @Test
    fun testApiFetchDataError() {
        mainViewModel.queryString.value = "hanoi"
        `when`(schedulerProvider.io).thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.ui).thenReturn(Schedulers.trampoline())

        `when`(repository.getForecasts("hanoi", false)).thenReturn(Single.error(Throwable()))
        mainViewModel.search(false)
        verify(observer).onChanged(LoadingState.LOADING)
        verify(observer).onChanged(LoadingState.ERROR)
        assert(mainViewModel.errorText.value == "Error loading data.")
    }

    @Test
    fun testApiFetchDataRefreshError() {
        mainViewModel.queryString.value = "hanoi"
        `when`(schedulerProvider.io).thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.ui).thenReturn(Schedulers.trampoline())

        `when`(repository.getForecasts("hanoi", false)).thenReturn(Single.error(Throwable()))
        mainViewModel.search(false)
        verify(observer).onChanged(LoadingState.LOADING)
        verify(observer).onChanged(LoadingState.ERROR)

        Mockito.reset(observer)
        `when`(repository.getForecasts("hanoi", true)).thenReturn(Single.error(Throwable()))
        mainViewModel.search(true)
        verify(observer).onChanged(LoadingState.REFRESHING)
        verify(observer).onChanged(LoadingState.ERROR)
        assert(mainViewModel.errorText.value == "Error loading data.")
    }

    @After
    fun finish() {
        mainViewModel.onCleared()
    }
}