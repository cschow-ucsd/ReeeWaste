package project.ucsd.reee_waste.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import project.ucsd.reee_waste.platformMessage
import project.ucsd.reee_waste.service.RwService
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val scope = MainScope() + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textviewMainHello.text = platformMessage()

        val service = RwService("5AC99F5F-F948-44F9-A925-345BCF8DE90B", "138246C6-FA44-4BEE-BD10-EADC26888365")
        scope.launch {
            TODO("test login service")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
