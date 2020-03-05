package project.ucsd.reee_waste.android

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
                "li.alan180@hotmail.com", "alan", "bruhbruh")
        val response = deferred.await() //Waits for response
        println(response)
        assertNotNull(response)
    }

    @Test
    fun loginAndValidUserTokenTest(): Unit = runBlocking {
        userToken = userToken ?: retrieveToken()
        val deferred = service.validateUserTokenAsync(userToken!!)
        val response = deferred.await()
        assertTrue(response)
    }

    @Test
    fun postItemTest(): Unit = runBlocking {
        val item = Item("", "",
                null, 50.01, "Computers", true, "Big Computer",
                "An epic computer")
        val deferred = service.postItemAsync(item)
        val response = deferred.await()
        assertNotNull(response)
    }

    @Test
    fun updateItemTest(): Unit = runBlocking {
        val item = Item("", "", null, 3.14, "Smartphones",
                true, "Nokia Phone", "A brick")
        val updateResponse = service.postItemAsync(item).await()

        val updatedItem = updateResponse.copy(price = Random.nextInt(100).toDouble())
        val deferred = service.updateItemAsync(updatedItem)
        val response = deferred.await()
        assertNotNull(response)
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
    fun deleteItemTest(): Unit = runBlocking {
        val item = Item("", "", null, 1337.0,
                "Lighting", true, "Limited Edition Lightbulb",
                "Limited edition lightbulb signed by Thomas Edison himself, no longer working")
        val updateResponse = service.postItemAsync(item).await()

        val deferred = service.deleteItemAsync(updateResponse.objectId)
        val response = deferred.await()
        println(response)
        assertNotNull(response)
    }
}