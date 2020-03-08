package project.ucsd.reee_waste.backendless.service

import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class WhereHelper(
){

    fun search(
            title: String,
            description: String,
            type: String,
            zipcode: String
    ): String {
        return Parameters.build {
            append("title", title)
            append("description", description)
            append("type", type)
            append("zipcode", zipcode)
            append("selling", "true")
        }.formUrlEncode()
    }
}