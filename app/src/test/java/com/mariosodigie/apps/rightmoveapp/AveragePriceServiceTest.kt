package com.mariosodigie.apps.rightmoveapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mariosodigie.apps.rightmoveapp.api.ApiError
import com.mariosodigie.apps.rightmoveapp.api.BaseServiceCallback
import com.mariosodigie.apps.rightmoveapp.averageprices.AveragePriceService
import com.mariosodigie.apps.rightmoveapp.averageprices.adapters.MISSING_PRICE_ERROR_MESSAGE
import com.mariosodigie.apps.rightmoveapp.averageprices.api.ApiService
import com.mariosodigie.apps.rightmoveapp.utils.ConnectivityCheck
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AveragePriceServiceTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var connectivityCheck: ConnectivityCheck

    @Mock
    lateinit var apiServe: ApiService

    private lateinit var service: AveragePriceService

    @Before
    fun setUp() {
        service = AveragePriceService(apiServe, connectivityCheck)
    }

    @Test
    fun pricesSuccessfullyRequested() {

        val  prices = listOf(100, 50, 200, 500)

        whenever(connectivityCheck.isConnectedToNetwork()).thenReturn(true)

        val callback = mock<BaseServiceCallback<List<Int>>>()
        val serviceCall = mock<Call<List<Int>>>()
        whenever(apiServe.requestPropertyPrices()).thenReturn(serviceCall)

        val response = mock<Response<List<Int>>>()
        whenever(response.isSuccessful).thenReturn(true)
        whenever(response.body()).thenReturn(prices)

        doAnswer {
            it.getArgument<Callback<List<Int>>>(0).onResponse(serviceCall,response)
        }.whenever(serviceCall).enqueue(any())

        service.getPrices(callback)

        verify(callback).onSuccess(prices)
    }

    @Test
    fun pricesRequestFailsWhenPhoneIsOffline() {

        whenever(connectivityCheck.isConnectedToNetwork()).thenReturn(false)

        val callback = mock<BaseServiceCallback<List<Int>>>()

        service.getPrices(callback)

        verify(callback).onError(ApiError.PhoneOffline)
    }

    @Test
    fun pricesRequestFailsWithThrowable() {

        whenever(connectivityCheck.isConnectedToNetwork()).thenReturn(true)

        val callback = mock<BaseServiceCallback<List<Int>>>()
        val serviceCall = mock<Call<List<Int>>>()
        whenever(apiServe.requestPropertyPrices()).thenReturn(serviceCall)

        val throwable = Throwable("Failure")

        doAnswer {
            it.getArgument<Callback<List<Int>>>(0).onFailure(serviceCall,throwable)
        }.whenever(serviceCall).enqueue(any())

        service.getPrices(callback)

        verify(callback).onError(throwable)
    }

    @Test
    fun pricesRequestFailsWhenPricePropertyIsMissing() {

        whenever(connectivityCheck.isConnectedToNetwork()).thenReturn(true)

        val callback = mock<BaseServiceCallback<List<Int>>>()
        val serviceCall = mock<Call<List<Int>>>()
        whenever(apiServe.requestPropertyPrices()).thenReturn(serviceCall)

        val throwable = Throwable(MISSING_PRICE_ERROR_MESSAGE)

        doAnswer {
            it.getArgument<Callback<List<Int>>>(0).onFailure(serviceCall,throwable)
        }.whenever(serviceCall).enqueue(any())

        service.getPrices(callback)

        verify(callback).onError(ApiError.MissingPrices)
    }
}