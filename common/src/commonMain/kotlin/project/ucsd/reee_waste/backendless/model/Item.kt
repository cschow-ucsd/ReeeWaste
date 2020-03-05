package project.ucsd.reee_waste.backendless.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
        val objectID: String = "",
        val seller: String,
        val buyer: String?,
        val price: Double,
        val type: String,
        val selling: Boolean,
        val title: String,
        val description: String
)

//typealias Item = SingleItemResponse
