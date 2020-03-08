package project.ucsd.reee_waste.android

import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import org.junit.After
import org.junit.Before
import org.junit.Test
import project.ucsd.reee_waste.backendless.model.Item
import project.ucsd.reee_waste.backendless.service.RwService
import project.ucsd.reee_waste.backendless.service.WhereHelper
import kotlin.random.Random

@UnstableDefault
@ImplicitReflectionSerializer
class RwServiceTest {
    private var userToken: String? = null

    private lateinit var service: RwService

    private suspend fun retrieveToken(): String {
        val deferred = service.loginAsync(
                BuildConfig.BACKENDLESS_LOGIN, BuildConfig.BACKENDLESS_PASSWORD)
        val response = deferred.await()
        return requireNotNull(response.userToken) { "User token must be null after login." }
    }

    @Before
    fun setupService() {
        service = RwService(
                appId = BuildConfig.BACKENDLESS_APP_ID,
                apiKey = BuildConfig.BACKENDLESS_API_KEY
        )
    }

    @After
    fun clearService() {
        service.close()
    }

    @Test
    fun loginTest(): Unit = runBlocking {
        val deferred = service.loginAsync(
                BuildConfig.BACKENDLESS_LOGIN, BuildConfig.BACKENDLESS_PASSWORD)
        val response = deferred.await() //Waits for response
        println(response)
        assertNotNull(response)
    }

    @Test
    fun createUserTest(): Unit = runBlocking {
        val deferred = service.createUserAsync(
                "li.alan180@hotmail.com", "alan", "bruhbruh", 92092,
                false)
        val userResponse = deferred.await() //Waits for response

        println(userResponse)
        assertNotNull(userResponse)

        val deleteResponse = service.client.delete<HttpResponse> {
            url(service.route("/data/Users/${userResponse.objectId}"))
            header(RwService.USER_TOKEN, userToken)
        }

        println(deleteResponse)
        assertNotNull(deleteResponse)
    }

    @Test
    fun loginAndValidUserTokenTest(): Unit = runBlocking {
        userToken = userToken ?: retrieveToken()
        val deferred = service.validateUserTokenAsync(userToken!!)
        val response = deferred.await()
        assertTrue(response)
    }

    @Test
    fun getDatabaseTest(): Unit = runBlocking {
        val deferred = service.getItemsAsync(10, 0)
        val response = deferred.await()
        println(response)
        assertNotNull(response)
    }

    @Test
    fun getItemTest(): Unit = runBlocking {
        val item = Item("", "", null, 700.21,
                "Large Household Appliances", true,
                "Smartfridge", "Working smartfridge, touchscreen cracked")
        val updateResponse = service.postItemAsync(item).await()

        val deferred = service.getItemAsync(updateResponse.objectId)
        val response = deferred.await()
        println(response)
        assertNotNull(response)
    }

    @Test
    fun postUpdateDeleteItemTest(): Unit = runBlocking {
        val item = Item("", "", null, 1337.0,
                "Lighting", true, "Limited Edition Lightbulb",
                "Limited edition lightbulb signed by Thomas Edison himself, no longer working")
        val postResponse = service.postItemAsync(item).await()
        assertNotNull(postResponse)

        val updatedItem = postResponse.copy(price = Random.nextInt(100).toDouble())
        val updateResponse = service.updateItemAsync(updatedItem).await()
        assertNotNull(updateResponse)

        val deleteResponse = service.deleteItemAsync(updateResponse.objectId).await()
        println(deleteResponse)
        assertNotNull(deleteResponse)
    }

    @Test
    fun failThenSucceedLoginTest(): Unit = runBlocking {
        val deleteDeferred = service.loginAsync("Goku", "KAMEHAMEHA");
        try {
            val deleteResponse = deleteDeferred.await()
        }
        catch(e: Exception){
            print(deleteDeferred.getCompletionExceptionOrNull())
        }
        val deferred = service.loginAsync(
                BuildConfig.BACKENDLESS_LOGIN, BuildConfig.BACKENDLESS_PASSWORD)
        val response = deferred.await() //Waits for response
        println(response)
        assertNotNull(response)
    }

    @Test
    fun buyItemTest(): Unit = runBlocking {
        val deferred = service.loginAsync(
                BuildConfig.BACKENDLESS_LOGIN, BuildConfig.BACKENDLESS_PASSWORD)
        val response = deferred.await()

        val item = Item("", "", null, 9999.99,
                "Automatic dispensers", true, "Free Money Dispenser",
                "Free money vending machine")
        val postResponse = service.postItemAsync(item).await()
        assertNotNull(postResponse)

        val buyResponse = service.buyItemAsync(postResponse.objectId, response.objectId).await()
        assertNotNull(buyResponse)
    }

    @Test
    fun searchTest(): Unit = runBlocking {
        val where = WhereHelper().search("Dispenser")

        val response = service.searchItemsAsync(10, 0, where).await()
        assertNotNull(response)
    }

}