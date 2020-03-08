package project.ucsd.reee_waste.android

import android.app.Application
import project.ucsd.reee_waste.backendless.model.User

class RwApplication: Application() {
    var currentUser: User? = null
}