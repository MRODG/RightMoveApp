package com.mariosodigie.apps.rightmoveapp.api

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.mariosodigie.apps.rightmoveapp.R
import com.mariosodigie.apps.rightmoveapp.extensions.observe

open class BaseServiceActivity : AppCompatActivity() {

    private var alertDialog: AlertDialog? = null

    /**
     * Subscribes to provided [ServiceError] and launches a general error dialog when an error occurs.
     * [onServiceErrorDialogAction] is invoked when user clicks dialog's action button, and
     */
    protected fun addErrorSource(source: LiveData<ServiceError>) = source.observe(this, ::onServiceError)

    protected open fun onServiceError(error: ServiceError){
        showErrorDialog(error)
    }

    private fun showErrorDialog(error: ServiceError) {

        alertDialog = AlertDialog.Builder(this)
            .apply {
                if (error.title != null) {
                    setTitle(error.title)
                }
                if(error.apiError.icon !=null){
                    setIcon(error.apiError.icon)
                }
            }
            .setMessage(error.message)
            .setPositiveButton(R.string.dialog_button_ok) { dialog, _ ->
                onServiceErrorDialogAction(dialog, error)
            }
            .show()
    }

    private fun onServiceErrorDialogAction(dialog: DialogInterface, error: ServiceError) = dialog.dismiss()

    override fun onStop() {
        alertDialog?.dismiss()
        super.onStop()
    }
}