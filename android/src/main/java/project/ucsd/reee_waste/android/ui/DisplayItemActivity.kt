package project.ucsd.reee_waste.android.ui

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_display_item.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.BuildConfig
import project.ucsd.reee_waste.android.R
import project.ucsd.reee_waste.android.RwApplication
import project.ucsd.reee_waste.android.RwServiceViewModel
import project.ucsd.reee_waste.backendless.service.RwService

@UnstableDefault
class DisplayItemActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 1357
        const val CAN_PURCHASE = "can_purchase"
        const val OBJECT_ID = "objectId"
    }

    private lateinit var objectId: String

    private val rwServiceViewModel: RwServiceViewModel by viewModels()
    private val rwService: RwService
        get() = rwServiceViewModel.rwServiceInstance

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_item)

        rwServiceViewModel.rwServiceInstance.userToken =
                getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
                        .getString(RwService.USER_TOKEN, null)


        val canPurchase = intent.getBooleanExtra(CAN_PURCHASE, false)
        if (!canPurchase) {
            buttonDisplayAddCart.visibility = View.GONE
            buttonDisplayPurchase.visibility = View.GONE
        } else {
            buttonDisplayAddCart.setOnClickListener {
                Toast.makeText(this, "Add to Cart coming soon!", Toast.LENGTH_SHORT).show()
            }
            buttonDisplayPurchase.setOnClickListener {
                purchaseItemAsync()
            }
        }

        objectId = intent.getStringExtra(OBJECT_ID)!!
        fetchItemAsync(objectId)
    }

    private fun purchaseItemAsync() = scope.launch {
        val user = (application as RwApplication).currentUser
        try {
            rwService.buyItemAsync(objectId, user!!.objectId).await()
        } catch (e: Exception) {
            rwErrorToast(e)
            return@launch
        }

        Toast.makeText(this@DisplayItemActivity, "Item purchased successfully!", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun fetchItemAsync(objectId: String) = scope.launch {
        val item = try {
            rwService.getItemAsync(objectId).await()
        } catch (e: Exception) {
            rwErrorToast(e)
            return@launch
        }

        textviewDisplayName.text = item.title
        textviewDisplayPrice.text = "${getString(R.string.currency)}${item.price}"
        textviewDisplayType.text = item.type
        textviewDisplayDescription.text = item.description

        val user = (application as RwApplication).currentUser!!
        if (item.ownerId == user.objectId) {
            buttonDisplayAddCart.visibility = View.GONE
            buttonDisplayPurchase.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
