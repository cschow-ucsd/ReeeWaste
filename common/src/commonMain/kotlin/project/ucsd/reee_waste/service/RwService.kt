package project.ucsd.reee_waste.service

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.request.*
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json.Companion.stringify

class RwService(
        private val appId: String,
        private val apiKey: String
) {
    companion object {
        const val BASE_URL = "https://api.backendless.com"
        const val USER_TOKEN = "user-token"
    }

    private var userToken: String? = null
    val client: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = defaultSerializer()
        }
    }

    private fun route(path: String) = "$BASE_URL/$appId/$apiKey$path"

    fun loginAsync(
            login: String,
            password: String
    ): Deferred<BackendlessResponse> = client.async {
        val response = client.post<BackendlessResponse> {
            url(route("/users/login"))
            contentType(ContentType.Application.Json)
            body = """
                {  
                    "login" : "$login",
                    "password" : "$password"
                }
            """.trimIndent()
        }//FormDataContent(parametersOf)
        if (response is BackendlessResponse.Login)
            userToken = response.userToken
        return@async response
    }

    fun createUserAsync(
            email: String,
            password: String
    ): Deferred<BackendlessResponse> = client.async {
        val response = client.post<BackendlessResponse> {
            url(route("/users/register"))
            contentType(ContentType.Application.Json)
            body = """
                {
                    "email": $email,
                    "password": $password
                }
            """.trimIndent()
        }
        return@async response
    }

    fun validateUserTokenAsync(
            token: String
    ): Deferred<Boolean> = client.async {
        val isValid = client.get<Boolean> {
            url(route("/users/isvalidusertoken/$token"))
        }
        if (isValid) userToken = token
        return@async isValid
    }

    fun postItemAsync(
            item: BackendlessResponse.Item
    ): Deferred<BackendlessResponse> = client.async {
        val response = client.post<BackendlessResponse> {
            url(route("/data/Item"))
            body = stringify(BackendlessResponse.Item.serializer(), item)
            contentType(ContentType.Application.Json)
            header(USER_TOKEN, userToken)
        }
        return@async response
    }

    fun updateItemAsync(
            item: BackendlessResponse.Item
    ): Deferred<BackendlessResponse> = client.async {
        val response = client.put<BackendlessResponse> {
            url(route("/data/Item/${item.objectID}"))
            body = stringify(BackendlessResponse.Item.serializer(), item)
            contentType(ContentType.Application.Json)
            header(USER_TOKEN, userToken)
        }
        return@async response
    }

    fun getDatabaseAsync(
            pageSize: Int,
            offset: Int,
            where: String?
    ): Deferred<BackendlessResponse> = client.async {
        val response = client.get<BackendlessResponse> {
            url(route("/services/rwservice/getitems2?pageSize=$pageSize&offset=$offset"))
            header(USER_TOKEN, userToken)
        }
        return@async response
    }

    fun getItemAsync(
            objectId: String
    ): Deferred<BackendlessResponse> = client.async {
        val response = client.get<BackendlessResponse> {
            url(route("/data/Item/$objectId"))
            header(USER_TOKEN, userToken)
        }
        return@async response
    }

    fun deleteItemAsync(
            objectId: String
    ): Deferred<BackendlessResponse> = client.async {
        val response = client.delete<BackendlessResponse> {
            url(route("/data/Item/$objectId"))
            header(USER_TOKEN, userToken)
        }
        return@async response
    }

}