package project.ucsd.reee_waste.backendless.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class User(
        val ___class: String,
        val blUserLocale: String,
        val created: Long,
        val email: String,
        val lastLogin: Long,
        val name: String,
        val objectId: String,
        val ownerId: String,
        val socialAccount: String,
        val updated: Long? = null,
        @SerialName("user-token")
        val userToken: String,
        val userStatus: String
)