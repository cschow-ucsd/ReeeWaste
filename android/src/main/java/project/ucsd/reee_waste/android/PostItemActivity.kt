package project.ucsd.reee_waste.android

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_post_item.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.BuildConfig
import project.ucsd.reee_waste.android.ui.rwErrorToast
import project.ucsd.reee_waste.backendless.model.Item
import project.ucsd.reee_waste.backendless.service.RwService
import kotlin.Exception

@UnstableDefault
class PostItemActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 2468
    }

    private val rwServiceViewModel: RwServiceViewModel by viewModels()
    private val rwService: RwService
        get() = rwServiceViewModel.rwServiceInstance

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)

        rwServiceViewModel.rwServiceInstance.userToken =
                getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
                        .getString(RwService.USER_TOKEN, null)

        buttonPostPost.setOnClickListener {
            postItemAsync()
        }

        spinnerPostType.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                RwService.EWASTE_TYPES)
    }

    private fun postItemAsync() = scope.launch {
        progressPostLoading.visibility = View.VISIBLE
        buttonPostPost.visibility = View.INVISIBLE

        val item = try {
            Item("", "", null,
                    edittextPostPrice.text.toString().toDouble(),
                    spinnerPostType.selectedItem.toString(),
                    true, edittextPostName.text.toString(),
                    edittextPostDescription.toString()
            )
        } catch (e: Exception) {
            Toast.makeText(this@PostItemActivity, R.string.invalid_input, Toast.LENGTH_SHORT).show()
            return@launch
        } finally {
            progressPostLoading.visibility = View.INVISIBLE
            buttonPostPost.visibility = View.VISIBLE
        }

        try {
            rwService.postItemAsync(item).await()
        } catch (e: Exception) {
            rwErrorToast(e)
            return@launch
        } finally {
            progressPostLoading.visibility = View.INVISIBLE
            buttonPostPost.visibility = View.VISIBLE
        }

        Toast.makeText(this@PostItemActivity, R.string.post_success, Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
