package project.ucsd.reee_waste.backendless.service

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Companion.nonstrict
import kotlinx.serialization.json.Json.Companion.stringify
import kotlinx.serialization.parse
import project.ucsd.reee_waste.backendless.model.Item
import project.ucsd.reee_waste.backendless.response.*

@UnstableDefault
@ImplicitReflectionSerializer
class RwService(
        private val appId: String,
        private val apiKey: String
) {
    companion object {
        const val BASE_URL = "https://api.backendless.com"
        const val USER_TOKEN = "user-token"
    }

    var userToken: String? = null
    val client: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(nonstrict)
        }
    }

    fun close() {
        client.close()
    }

    private fun route(path: String) = "$BASE_URL/$appId/$apiKey$path"

    private suspend inline fun <reified T: Any> HttpResponse.errorAwareReceive(
    ): T = if (status == HttpStatusCode.OK) {
        val text = readText().also { println("HttpResponse body: $it") }
        Json.parse(text.trimIndent())
//        receive<T>()
    } else {
        val errorResponse = receive<ErrorResponse>()
        throw BackendlessHttpException(errorResponse.message)
    }

    fun loginAsync(
            login: String,
            password: String
    ): Deferred<UserResponse> = client.async {
        val response = client.post<HttpResponse> {
            url(route("/users/login"))
            body = """
                {  
                    "login" : "$login",
                    "password" : "$password"
                }
            """.trimIndent()
        }
        return@async response
                .errorAwareReceive<UserResponse>()
                .also { userToken = it.userToken }
    }

    fun createUserAsync(
            email: String,
            password: String
    ): Deferred<UserResponse> = client.async {
        val response = client.post<HttpResponse> {
            url(route("/users/register"))
            body = """
                {
                    "email": $email,
                    "password": $password
                }
            """.trimIndent()
        }
        return@async response.errorAwareReceive<UserResponse>()
    }

    fun validateUserTokenAsync(
            token: String
    ): Deferred<Boolean> = client.async {
        val response = client.get<HttpResponse> {
            url(route("/users/isvalidusertoken/$token"))
        }
        response.errorAwareReceive<Boolean>()
                .also { if (it) userToken = token }
    }

    fun postItemAsync(
            item: Item
    ): Deferred<SingleItemResponse> = client.async {
        val response = client.post<HttpResponse> {
            url(route("/data/Item"))
            body = stringify(Item.serializer(), item)
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<SingleItemResponse>()
    }

    fun updateItemAsync(
            item: Item
    ): Deferred<SingleItemResponse> = client.async {
        val response = client.put<HttpResponse> {
            url(route("/data/Item/${item.objectId}"))
            body = stringify(Item.serializer(), item)
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<SingleItemResponse>()
    }

    fun getItemsAsync(
            pageSize: Int,
            offset: Int,
            where: String?
    ): Deferred<ItemsListResponse> = client.async {
        val response = client.get<HttpResponse> {
            url(route("/services/rwservice/getitems2?pageSize=$pageSize&offset=$offset"))
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<ItemsListResponse>()
    }

    fun getItemAsync(
            objectId: String
    ): Deferred<SingleItemResponse> = client.async {
        val response = client.get<HttpResponse> {
            url(route("/data/Item/$objectId"))
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<SingleItemResponse>()
    }

    fun deleteItemAsync(
            objectId: String
    ): Deferred<DeleteItemResponse> = client.async {
        val response = client.delete<HttpResponse> {
            url(route("/data/Item/$objectId"))
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<DeleteItemResponse>()
    }

}