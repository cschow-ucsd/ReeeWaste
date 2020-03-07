package project.ucsd.reee_waste.backendless.service

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json.Companion.nonstrict
import kotlinx.serialization.json.Json.Companion.stringify
import project.ucsd.reee_waste.backendless.model.Item
import project.ucsd.reee_waste.backendless.response.*
import kotlin.coroutines.CoroutineContext

@UnstableDefault
class RwService(
        private val appId: String,
        private val apiKey: String
) : CoroutineScope {
    companion object {
        const val BASE_URL = "https://api.backendless.com"
        const val USER_TOKEN = "user-token"
    }

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job

    var userToken: String? = null
    val client: HttpClient = HttpClient {
        expectSuccess = false
        install(JsonFeature) {
            serializer = KotlinxSerializer(nonstrict)
        }
    }

    fun close() {
        job.cancel()
        client.close()
    }

    fun route(path: String) = "$BASE_URL/$appId/$apiKey$path"

    private suspend inline fun <reified T : Any> HttpResponse.errorAwareReceive(
    ): T = if (status == HttpStatusCode.OK) {
        receive()
    } else {
        val errorResponse = receive<ErrorResponse>()
        throw BackendlessHttpException(errorResponse.message)
    }

    fun loginAsync(
            login: String,
            password: String
    ): Deferred<UserResponse> = async {
        val response = client.post<HttpResponse> {
            url(route("/users/login"))
            body = """
                {
                    "login" : "$login",
                    "password" : "$password"
                }
            """.trimIndent()
        }
        return@async response.errorAwareReceive<UserResponse>()
                .also { userToken = it.userToken }
    }

    fun createUserAsync(
            email: String,
            name: String,
            password: String
    ): Deferred<UserResponse> = async {
        val response = client.post<HttpResponse> {
            url(route("/users/register"))
            body = """
                {
                    "email": "$email",
                    "name": "$name",
                    "password": "$password"
                }
            """.trimIndent()
        }
        return@async response.errorAwareReceive<UserResponse>()
    }

    fun validateUserTokenAsync(
            token: String
    ): Deferred<Boolean> = async {
        val response = client.get<HttpResponse> {
            url(route("/users/isvalidusertoken/$token"))
        }
        response.errorAwareReceive<Boolean>()
                .also { if (it) userToken = token }
    }

    fun postItemAsync(
            item: Item
    ): Deferred<SingleItemResponse> = async {
        val response = client.post<HttpResponse> {
            url(route("/data/Item"))
            body = stringify(Item.serializer(), item)
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<SingleItemResponse>()
    }

    fun updateItemAsync(
            item: Item
    ): Deferred<SingleItemResponse> = async {
        val response = client.put<HttpResponse> {
            url(route("/data/Item/${item.objectId}"))
            body = stringify(Item.serializer(), item).also(::println)
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<SingleItemResponse>()
    }

    fun getItemsAsync(
            pageSize: Int,
            offset: Int,
            where: String = ""
    ): Deferred<ItemsListResponse> = async {
        val query = "pageSize=$pageSize&offset=$offset&where=$where"
        val response = client.get<HttpResponse> {
            url(route("/services/rwservice/getitems2?$query"))
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<ItemsListResponse>()
    }

    fun getItemAsync(
            objectId: String
    ): Deferred<SingleItemResponse> = async {
        val response = client.get<HttpResponse> {
            url(route("/data/Item/$objectId"))
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<SingleItemResponse>()
    }

    fun deleteItemAsync(
            objectId: String
    ): Deferred<DeleteItemResponse> = async {
        val response = client.delete<HttpResponse> {
            url(route("/data/Item/$objectId"))
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<DeleteItemResponse>()
    }

    fun buyItemAsync(
            objectId: String,
            userId: String
    ): Deferred<SingleItemResponse> = async {
        val response = client.put<HttpResponse> {
            url(route("/data/Item/$objectId"))
            body = """
                {
                    "selling" : false
                }
            """.trimIndent()
            header(USER_TOKEN, userToken)
        }
        client.post<HttpResponse> {
            url(route("/data/Item/$objectId/buyer:Users:1"))
            body = """
                {
                    "buyer" : $userId
                }
            """.trimIndent()
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<SingleItemResponse>()
    }

    fun retrieveUserAsync(
            userToken: String
    ): Deferred<UserResponse> = async {
        val response = client.get<HttpResponse> {
            url(route("/services/rwservice/retrieveUser"))
            header(USER_TOKEN, userToken)
        }
        return@async response.errorAwareReceive<UserResponse>()
    }

}