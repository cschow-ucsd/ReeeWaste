package project.ucsd.reee_waste.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.ui.LoadingFragment

@UnstableDefault
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.mainContainer, LoadingFragment())
            }
        }
    }
}
