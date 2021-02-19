package com.mariosodigie.apps.rightmoveapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mariosodigie.apps.rightmoveapp.api.BaseServiceCallback
import com.mariosodigie.apps.rightmoveapp.api.ServiceError
import com.mariosodigie.apps.rightmoveapp.averageprices.AveragePriceService
import com.mariosodigie.apps.rightmoveapp.averageprices.AveragePriceViewModel
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class AveragePriceViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var averagePriceService: AveragePriceService

    private lateinit var viewModel: AveragePriceViewModel

    @Before
    fun setUp() {
        viewModel = AveragePriceViewModel(averagePriceService, context)
    }

    @Test
    fun getsPricesViewModelInit() {
        verify(averagePriceService).getPrices(any())
    }

    @Test
    fun signalsRequestLiveData() {
        val progressObserver = mock<Observer<Boolean>>()
        viewModel.requestInProgress.observeForever(progressObserver)

        verify(progressObserver).onChanged(true)
    }

    @Test
    fun getsPricesFromService() {
        viewModel.getCurrentAveragePrice()
        verify(averagePriceService, times(2)).getPrices(any())
    }

    @Test
    fun signalsAveragePriceLiveDataWhenRequested() {

        val  prices = listOf(100, 50, 200, 500)
        val average = "212.50"

        doAnswer {
            it.getArgument<BaseServiceCallback<List<Int>>>(0).onSuccess(prices)
        }.whenever(averagePriceService).getPrices(any())

        val observer = mock<Observer<String>>()
        viewModel.avgPriceLiveData.observeForever(observer)
        viewModel.getCurrentAveragePrice()
        verify(observer).onChanged(average)
    }

    @Test
    fun signalsErrorWhenLightScoreReportFails() {
        val errorMessage = "failure"

        doAnswer {
            it.getArgument<BaseServiceCallback<String>>(0).onError(RuntimeException(errorMessage))
        }.whenever(averagePriceService).getPrices(any())

        whenever(context.getString(any())).thenReturn(errorMessage)

        val errorObserver = mock<Observer<ServiceError>>()
        viewModel.serviceError.observeForever(errorObserver)

        viewModel.getCurrentAveragePrice()

        verify(errorObserver).onChanged(argThat {
            message == errorMessage
        })
    }

}