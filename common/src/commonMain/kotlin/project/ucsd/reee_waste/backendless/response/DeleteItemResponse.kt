package project.ucsd.reee_waste.backendless.response

import kotlinx.serialization.Serializable

@Serializable
data class DeleteItemResponse(
        val deletionTime: Long
)