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
        val lastLogin: Long? = null,
        val name: String,
        val objectId: String,
        val updated: Long? = null,
        @SerialName("user-token")
        val userToken: String? = null,
        val userStatus: String
)