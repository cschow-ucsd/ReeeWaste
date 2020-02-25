package project.ucsd.reee_waste.service

import kotlinx.serialization.Serializable

@Serializable
data class Item(
        val seller: String,
        val buyer: String?,
        val price : Double,
        val type : String,
        val selling: Boolean,
        val title: String,
        val description: String
)