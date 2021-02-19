package com.mariosodigie.apps.rightmoveapp.api

interface BaseServiceCallback<T> {
    fun onSuccess(response: T)
    fun onError(error: ApiError)
    fun onError(throwable: Throwable)
}