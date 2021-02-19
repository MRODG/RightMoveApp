package com.mariosodigie.apps.rightmoveapp.averageprices

import com.mariosodigie.apps.rightmoveapp.api.ApiError
import com.mariosodigie.apps.rightmoveapp.api.BaseServiceCallback
import com.mariosodigie.apps.rightmoveapp.averageprices.adapters.MISSING_PRICE_ERROR_MESSAGE
import com.mariosodigie.apps.rightmoveapp.averageprices.api.ApiService
import com.mariosodigie.apps.rightmoveapp.utils.ConnectivityCheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AveragePriceService(private val apiServe: ApiService,
                          private val connectivityCheck: ConnectivityCheck){

    fun getPrices(callback: BaseServiceCallback<List<Int>>){

        if (!connectivityCheck.isConnectedToNetwork()) {
            callback.onError(ApiError.PhoneOffline)
            return
        }

        apiServe.requestPropertyPrices().enqueue(object: Callback<List<Int>> {
            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                if (t.message?.contains(MISSING_PRICE_ERROR_MESSAGE) == true) callback.onError(ApiError.MissingPrices)
                else callback.onError(t)
            }
            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                if(response.isSuccessful){
                    callback.onSuccess(response.body()!!)
                }
                else{
                    callback.onError(Throwable(response.message()))
                }
            }
        })
    }

}