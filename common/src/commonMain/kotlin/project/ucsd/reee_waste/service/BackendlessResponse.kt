package project.ucsd.reee_waste.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
sealed class BackendlessResponse {
    @Serializable
    data class Error(
            val code: Int,
            val message: String
    ) : BackendlessResponse()

    @Serializable
    data class Login(
            val ___class: String,
            val blUserLocale: String,
            val created: Long,
            val email: String,
            val lastLogin: Long,
            val name: String,
            val objectId: String,
            val ownerId: String,
            val socialAccount: String,
            val updated: JsonObject,
            @SerialName("user-token")
            val userToken: String,
            val userStatus: String
    ): BackendlessResponse()
}