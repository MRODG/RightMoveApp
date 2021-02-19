package com.mariosodigie.apps.rightmoveapp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.mariosodigie.apps.rightmoveapp.averageprices.AveragePriceActivity
import com.nhaarman.mockitokotlin2.whenever
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.mariosodigie.apps.rightmoveapp.api.ApiError
import com.mariosodigie.apps.rightmoveapp.api.ServiceError
import com.mariosodigie.apps.rightmoveapp.averageprices.AveragePriceViewModel
import com.mariosodigie.apps.rightmoveapp.rules.ViewModelRule
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

class AveragePriceActivityTest{

    @get:Rule
    val testRule = IntentsTestRule(AveragePriceActivity::class.java, true, false)

    @get:Rule
    val viewModelRule = ViewModelRule()

    private val requestInProgress = MutableLiveData<Boolean>()
    private val avgPriceLiveData = MutableLiveData<String>()
    private val serviceError = MutableLiveData<ServiceError>()

    @Mock
    lateinit var viewModel: AveragePriceViewModel

    lateinit var context: Context

    @Before
    fun setUp() {
        whenever(viewModel.requestInProgress).thenReturn(requestInProgress)
        whenever(viewModel.avgPriceLiveData).thenReturn(avgPriceLiveData)
        whenever(viewModel.serviceError).thenReturn(serviceError)
        context = InstrumentationRegistry.getInstrumentation().targetContext;
        testRule.launchActivity(null)
    }

    @Test
    fun displaysAveragePrice() {
        avgPriceLiveData.postValue("255000.00")
        onView(withText(containsString("£ 255000.00")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displaysPhoneOfflineError() {

        serviceError.postValue(ServiceError(context.getString(ApiError.PhoneOffline.title), context.getString(ApiError.PhoneOffline.message), ApiError.PhoneOffline))
        onView(withText(containsString("No internet connection detected"))).check(matches(isDisplayed()))
        onView(withText(CoreMatchers.containsString("Please ensure your phone has an active internet connection and try again."))).check(matches(isDisplayed()))
    }

    @Test
    fun displaysMissingPricesError() {
        serviceError.postValue(ServiceError(context.getString(ApiError.MissingPrices.title), context.getString(ApiError.MissingPrices.message), ApiError.MissingPrices))
        onView(withText(containsString("Prices unavailable"))).check(matches(isDisplayed()))
        onView(withText(containsString("Property prices are currently not available. Please try again"))).check(matches(isDisplayed()))
    }

    @Test
    fun displaysGenericError() {
        serviceError.postValue(ServiceError(context.getString(ApiError.Generic.title), context.getString(ApiError.Generic.message), ApiError.Generic))
        onView(withText(containsString("Something went wrong"))).check(matches(isDisplayed()))
        onView(withText(containsString("Please try again"))).check(matches(isDisplayed()))
    }

    @Test
    fun dismissesErrorDialog() {
        serviceError.postValue(ServiceError(context.getString(ApiError.Generic.title), context.getString(ApiError.Generic.message), ApiError.Generic))
        onView(withText(R.string.dialog_button_ok)).perform(ViewActions.click())
        onView(withText(containsString("Something went wrong"))).check(
            ViewAssertions.doesNotExist()
        )
    }
}