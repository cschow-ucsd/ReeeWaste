package project.ucsd.reee_waste.android.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.*
import project.ucsd.reee_waste.android.ui.rwErrorToast
import project.ucsd.reee_waste.backendless.service.BackendlessHttpException
import project.ucsd.reee_waste.backendless.service.RwService
import java.lang.Exception

@UnstableDefault
class LoadingFragment : Fragment() {
    private var userToken by stringPreference(RwService.USER_TOKEN, null)
    private val rwViewModel: RwServiceViewModel by activityViewModels()
    private val rwService: RwService
        get() = rwViewModel.rwServiceInstance

    private val scope = MainScope()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateUserTokenAsync()
    }

    private fun validateUserTokenAsync(
    ) = scope.launch {
        val isValid = try {
            (userToken != null)
                    && rwService.validateUserTokenAsync(userToken!!).await()
        } catch (e: Exception) {
            rwErrorToast(e)
            return@launch
        }

        if (isValid) {
            // retrieve user and set to application
            (activity?.application as RwApplication)
                    .currentUser = rwService.retrieveUserAsync(userToken!!).await()

            // show data if valid
            val intent = Intent(activity, NavigationActivity::class.java)
            startActivity(intent)
            activity?.finish()
        } else {
            parentFragmentManager.commit {
                replace(R.id.mainContainer, LoginFragment())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
