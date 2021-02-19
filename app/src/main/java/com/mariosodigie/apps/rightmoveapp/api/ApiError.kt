package com.mariosodigie.apps.rightmoveapp.api

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mariosodigie.apps.rightmoveapp.R

enum class ApiError(@StringRes val title: Int, @StringRes val message: Int, @DrawableRes val icon: Int? = null) {

    Generic(R.string.error_title_generic, R.string.error_message_generic),
    PhoneOffline(R.string.error_title_offline, R.string.error_message_offline, R.drawable.ic_wifi_off_black_18dp),
    MissingPrices(R.string.error_title_missing_prices, R.string.error_message_missing_prices)
}