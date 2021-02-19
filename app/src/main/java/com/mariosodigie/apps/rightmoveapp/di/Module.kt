package com.mariosodigie.apps.rightmoveapp.di

import com.mariosodigie.apps.rightmoveapp.averageprices.AveragePriceService
import com.mariosodigie.apps.rightmoveapp.averageprices.api.ApiService
import com.mariosodigie.apps.rightmoveapp.utils.ConnectivityCheck
import org.koin.dsl.module

val appModule = module {

    single { ConnectivityCheck(get()) }
    single { AveragePriceService(ApiService.get(), get()) }

}