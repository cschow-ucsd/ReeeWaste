package project.ucsd.reee_waste.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.ui.LoadingFragment

@UnstableDefault
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.mainContainer, LoadingFragment())
            }
        }
    }
}
