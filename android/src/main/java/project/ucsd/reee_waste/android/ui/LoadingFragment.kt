package project.ucsd.reee_waste.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.R
import project.ucsd.reee_waste.android.RwServiceViewModel
import project.ucsd.reee_waste.android.stringPreference
import project.ucsd.reee_waste.backendless.service.RwService

@UnstableDefault
class LoadingFragment : Fragment() {
    private var rwUserToken by stringPreference(RwService.USER_TOKEN, null)
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
        val isValid = (rwUserToken != null)
                && rwService.validateUserTokenAsync(rwUserToken!!).await()

        parentFragmentManager.commit {
            replace(R.id.mainContainer,
                    if (isValid) DashboardFragment()
                    else LoginFragment()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
