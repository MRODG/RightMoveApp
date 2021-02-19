package com.mariosodigie.apps.rightmoveapp.di

import com.mariosodigie.apps.rightmoveapp.averageprices.AveragePriceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { AveragePriceViewModel(get(), get()) }
}