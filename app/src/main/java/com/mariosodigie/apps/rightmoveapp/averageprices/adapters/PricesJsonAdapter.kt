package com.mariosodigie.apps.rightmoveapp.averageprices.adapters

import com.mariosodigie.apps.rightmoveapp.extensions.readArray
import com.mariosodigie.apps.rightmoveapp.extensions.readObject
import com.mariosodigie.apps.rightmoveapp.extensions.skipNameAndValue
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader

class PricesJsonAdapter {

    @FromJson
    fun eventFromJson(reader: JsonReader): List<Int> {

        val priceKey = JsonReader.Options.of("price") as JsonReader.Options
        val  prices = mutableListOf<Int>()

        reader.readObject {
            reader.nextName()
            reader.readArray {
                var price = 0
                reader.readObject {
                    when (reader.selectName(priceKey)) {
                        0 -> price = reader.nextInt()
                        else -> reader.skipNameAndValue()
                    }
                }
                if (price < 1 ) {
                    throw JsonDataException(MISSING_PRICE_ERROR_MESSAGE)
                }
                prices.add(price)
            }
        }
        return prices
    }
}
const val MISSING_PRICE_ERROR_MESSAGE = "Missing Price Property Field"