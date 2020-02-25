package project.ucsd.reee_waste.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import project.ucsd.reee_waste.platformMessage
import project.ucsd.reee_waste.service.RwService

class MainActivity : AppCompatActivity(){
    private val TAG = "MainActivity"
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
