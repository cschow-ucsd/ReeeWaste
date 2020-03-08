package project.ucsd.reee_waste.android.ui

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import project.ucsd.reee_waste.android.R
import project.ucsd.reee_waste.backendless.service.BackendlessHttpException
import java.lang.Exception

fun Fragment.rwErrorToast(e: Exception) {
    Toast.makeText(context,
            if (e is BackendlessHttpException) e.message
            else getString(R.string.generic_error),
            Toast.LENGTH_SHORT).show()
}

fun Activity.rwErrorToast(e: Exception) {
    Toast.makeText(this,
            if (e is BackendlessHttpException) e.message
            else getString(R.string.generic_error),
            Toast.LENGTH_SHORT).show()
}