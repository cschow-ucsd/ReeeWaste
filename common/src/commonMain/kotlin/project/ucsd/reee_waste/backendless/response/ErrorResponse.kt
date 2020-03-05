package project.ucsd.reee_waste.backendless.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
        val code: Int,
        val message: String
)