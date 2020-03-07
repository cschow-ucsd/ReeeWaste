package project.ucsd.reee_waste.android.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault

import project.ucsd.reee_waste.android.R
import project.ucsd.reee_waste.android.RwServiceViewModel
import project.ucsd.reee_waste.backendless.service.BackendlessHttpException
import project.ucsd.reee_waste.backendless.service.RwService

/**
 * A simple [Fragment] subclass.
 */
@UnstableDefault
class RegisterFragment : Fragment() {
    private val rwServiceViewModel: RwServiceViewModel by activityViewModels()
    private val rwService: RwService
        get() = rwServiceViewModel.rwServiceInstance

    private val scope = MainScope()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonRegisterRegister.setOnClickListener {
            registerAsync()
        }

        val spinnerAdapter = ArrayAdapter<String>(
                context!!, android.R.layout.simple_spinner_dropdown_item,
                arrayOf(getString(R.string.acc_buyer_seller), getString(R.string.acc_ewaste_center)))
        spinnerRegisterType.adapter = spinnerAdapter
    }

    private fun registerAsync() = scope.launch {
        progressRegisterLoading.visibility = View.VISIBLE
        buttonRegisterRegister.visibility = View.INVISIBLE

        try {
            rwService.createUserAsync(
                    email = edittextRegisterEmail.text.toString(),
                    name = edittextRegisterName.text.toString(),
                    password = edittextRegisterPassword.text.toString(),
                    zipCode = edittextRegisterZipcode.text.toString().toIntOrNull() ?: 0,
                    isCenter = spinnerRegisterType.selectedItem == getString(R.string.acc_ewaste_center)
            ).await()
        } catch (e: BackendlessHttpException) {
            progressRegisterLoading.visibility = View.INVISIBLE
            buttonRegisterRegister.visibility = View.VISIBLE
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            return@launch
        }

        Toast.makeText(context, "User created successfully!", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }
}
