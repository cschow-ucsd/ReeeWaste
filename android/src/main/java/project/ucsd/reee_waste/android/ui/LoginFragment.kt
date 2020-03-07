package project.ucsd.reee_waste.android.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.R
import project.ucsd.reee_waste.android.RwServiceViewModel
import project.ucsd.reee_waste.android.stringPreference
import project.ucsd.reee_waste.backendless.service.BackendlessHttpException
import project.ucsd.reee_waste.backendless.service.RwService

@UnstableDefault
class LoginFragment : Fragment() {
    private var rwUserToken: String? by stringPreference(RwService.USER_TOKEN, null)
    private val rwServiceViewModel: RwServiceViewModel by activityViewModels()
    private val rwService: RwService
        get() = rwServiceViewModel.rwServiceInstance

    private val scope = MainScope()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonLoginLogin.setOnClickListener {
            loginAsync()
        }
        textviewLoginRegister.setOnClickListener {
            launchRegisterFragment()
        }
    }

    private fun launchRegisterFragment() {
        parentFragmentManager.commit {
            addToBackStack(null)
            setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            replace(R.id.mainContainer, RegisterFragment())
        }
    }

    private fun loginAsync() = scope.launch {
        progressLoginLoading.visibility = View.VISIBLE
        buttonLoginLogin.visibility = View.INVISIBLE

        val loginResponse = try {
            rwService.loginAsync(
                    login = edittextLoginId.text.toString(),
                    password = edittextLoginPassword.text.toString()
            ).await()
        } catch (e: BackendlessHttpException) {
            progressLoginLoading.visibility = View.INVISIBLE
            buttonLoginLogin.visibility = View.VISIBLE
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            return@launch
        }

        rwUserToken = loginResponse.userToken
        Toast.makeText(
                context, "${getString(R.string.message_welcome)} ${loginResponse.name}!",
                Toast.LENGTH_SHORT).show()

        parentFragmentManager.commit {
            replace(R.id.mainContainer, DashboardFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
