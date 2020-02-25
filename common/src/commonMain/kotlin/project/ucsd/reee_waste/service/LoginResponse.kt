package project.ucsd.reee_waste.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
        val objectId: String,
        @SerialName("user-token")
        val userToken: String
)