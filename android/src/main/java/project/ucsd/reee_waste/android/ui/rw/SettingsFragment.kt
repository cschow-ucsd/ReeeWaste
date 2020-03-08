package project.ucsd.reee_waste.android.ui.rw


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.LoginActivity

import project.ucsd.reee_waste.android.R
import project.ucsd.reee_waste.android.stringPreference
import project.ucsd.reee_waste.backendless.service.RwService

@UnstableDefault
class SettingsFragment : Fragment() {
    private var userToken: String? by stringPreference(RwService.USER_TOKEN, null)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSettingsLogout.setOnClickListener {
            userToken = null
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}
