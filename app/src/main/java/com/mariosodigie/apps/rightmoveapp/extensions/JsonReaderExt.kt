package com.mariosodigie.apps.rightmoveapp.extensions

import com.squareup.moshi.JsonReader

fun JsonReader.skipNameAndValue() {
    skipName()
    skipValue()
}
inline fun JsonReader.readObject(body: () -> Unit) {
    beginObject()
    while (hasNext()) {
        body()
    }
    endObject()
}

inline fun JsonReader.readArray(body: () -> Unit) {
    beginArray()
    while (hasNext()) {
        body()
    }
    endArray()
}