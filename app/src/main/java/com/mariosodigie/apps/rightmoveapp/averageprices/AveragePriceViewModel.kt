package com.mariosodigie.apps.rightmoveapp.averageprices

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mariosodigie.apps.rightmoveapp.api.BaseServiceViewModel

class AveragePriceViewModel(private val service: AveragePriceService, context: Context): BaseServiceViewModel(context){

    init {
        getCurrentAveragePrice()
    }

    private val _avgPriceLiveData = MutableLiveData<String>()
    val avgPriceLiveData: LiveData<String> = _avgPriceLiveData

    final fun getCurrentAveragePrice(){
        service.getPrices(ServiceCallback { priceList ->
            _avgPriceLiveData.value = calculateAveragePrice(priceList)
        })
    }

    private fun calculateAveragePrice(priceList: List<Int>): String {
        return String.format("%.2f", priceList.run { sum().toFloat() / size })
    }
}