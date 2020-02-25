package project.ucsd.reee_waste.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.plus
import project.ucsd.reee_waste.platformMessage

class MainActivity : AppCompatActivity(){
    private val scope = MainScope() + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textviewMainHello.text = platformMessage()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
