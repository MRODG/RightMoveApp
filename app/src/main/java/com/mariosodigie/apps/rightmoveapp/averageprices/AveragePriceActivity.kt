package com.mariosodigie.apps.rightmoveapp.averageprices

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.mariosodigie.apps.rightmoveapp.R
import com.mariosodigie.apps.rightmoveapp.api.BaseServiceActivity
import com.mariosodigie.apps.rightmoveapp.extensions.formatString
import com.mariosodigie.apps.rightmoveapp.extensions.observe
import kotlinx.android.synthetic.main.activity_average_price.*

class AveragePriceActivity : BaseServiceActivity() {

    private val viewModel: AveragePriceViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_average_price)

        addErrorSource(viewModel.serviceError)

        viewModel.requestInProgress.observe(this) {inProgress ->
            showProgress(inProgress)
        }

        viewModel.avgPriceLiveData .observe(this){ priceData ->
            averagePriceTV.formatString(R.string.activity_average_price_answer, priceData )
        }

    }

    private fun showProgress(inProgress: Boolean) {
        serviceProgress.apply {
            visibility = if(inProgress) View.VISIBLE else View.GONE
        }
    }
}