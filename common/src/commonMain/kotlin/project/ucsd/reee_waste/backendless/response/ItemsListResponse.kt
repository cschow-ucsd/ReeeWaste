package project.ucsd.reee_waste.backendless.response

import kotlinx.serialization.Serializable
import project.ucsd.reee_waste.backendless.model.Item

@Serializable
data class ItemsListResponse(
        val results: List<Item>
)