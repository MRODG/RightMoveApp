package com.mariosodigie.apps.rightmoveapp.api

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mariosodigie.apps.rightmoveapp.utils.OpenForTesting
import com.mariosodigie.apps.rightmoveapp.utils.runOnMainThread

data class ServiceError(val title: String?, val message: String, val apiError: ApiError = ApiError.Generic)
@OpenForTesting
abstract class BaseServiceViewModel(protected val context: Context): ViewModel() {

    private var _requestInProgress = MutableLiveData<Boolean>()
    val requestInProgress: LiveData<Boolean> = _requestInProgress

    private val _serviceError = MutableLiveData<ServiceError>()
    val serviceError: LiveData<ServiceError> = _serviceError

    protected inner class ServiceCallback<T> (private val handler: (T) -> Unit) : BaseServiceCallback<T> {
        init {
            _requestInProgress.value = true
        }

        override fun onSuccess(response: T) =  runOnMainThread{
            _requestInProgress.value = false
            handler(response)
        }

        override fun onError(error: ApiError) {
            _requestInProgress.value = false
            _serviceError.value =
                ServiceError(
                    context.getString(error.title), context.getString(error.message), error
                )
        }

        override fun onError(throwable: Throwable) {
            onError(ApiError.Generic)
        }
    }
}