package project.ucsd.reee_waste.backendless.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
        val objectId: String = "",
        val ownerId: String?,
        val buyer: String? = null,
        val price: Double,
        val type: String,
        val selling: Boolean,
        val title: String,
        val description: String
)
