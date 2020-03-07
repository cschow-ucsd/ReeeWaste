package project.ucsd.reee_waste.android

import androidx.lifecycle.ViewModel
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.backendless.service.RwService

class RwServiceViewModel: ViewModel() {
    @UnstableDefault
    val rwServiceInstance: RwService = RwService(
            appId = BuildConfig.BACKENDLESS_APP_ID,
            apiKey = BuildConfig.BACKENDLESS_API_KEY
    )

    @UnstableDefault
    override fun onCleared() {
        super.onCleared()
        rwServiceInstance.close()
    }
}