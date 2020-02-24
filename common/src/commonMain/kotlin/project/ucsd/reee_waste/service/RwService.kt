package project.ucsd.reee_waste.service

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.client.statement.response
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class RwService(
        val appId: String,
        val apiKey: String
) {
    companion object {
        const val BASE_URL = "https://api.backendless.com"
    }

    private var userToken: String? = null
    val client: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = defaultSerializer()
        }
        defaultRequest {
            host = "$BASE_URL/$appId/$apiKey"
            header("user-token", userToken)
            contentType(ContentType.Application.Json)
        }
    }

    fun login(
            login: String,
            password: String
    ): Deferred<LoginResponse> = client.async {
        val response = client.post<RawLoginResponse> {
            url("/users/login")
            body = """
                {
                    "login": $login,
                    "password": $password
                }
            """.trimIndent()
        }
        userToken = response.`user-token`
        return@async LoginResponse(response.objectId, response.`user-token`)
    }

    fun validateUserToken(
            token: String
    ): Deferred<Boolean> = client.async {
        val isValid = client.get<Boolean>("/users/isvalidusertoken/$token")
        if (isValid) userToken = token
        return@async isValid
    }
}

private data class RawLoginResponse(
        val objectId: String,
        val `user-token`: String
)