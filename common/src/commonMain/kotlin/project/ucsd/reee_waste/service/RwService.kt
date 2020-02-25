package project.ucsd.reee_waste.service

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
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
                    "login": $login,
                    "password": $password,
                }
            """.trimIndent()
        }
        if (response is BackendlessResponse.Login)
            userToken = response.userToken
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
            item: Item
    ): Deferred<HttpResponse> = client.async {
        val response = client.post<HttpResponse> {
            url(route("/data/Item"))
            body = stringify(Item.serializer(), item)
            contentType(ContentType.Application.Json)
            header(USER_TOKEN, userToken)
        }
        return@async response
    }
}