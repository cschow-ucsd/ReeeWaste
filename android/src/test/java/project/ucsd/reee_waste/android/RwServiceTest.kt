package project.ucsd.reee_waste.android

import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import project.ucsd.reee_waste.backendless.model.Item
import project.ucsd.reee_waste.backendless.service.RwService
import kotlin.random.Random
import org.junit.runner.RunWith

@Suppress("EXPERIMENTAL_API_USAGE")
class RwServiceTest {
    private var userToken: String? = null

    private lateinit var service: RwService

    private suspend fun retrieveToken(): String {
        val deferred = service.loginAsync(
                BuildConfig.BACKENDLESS_LOGIN, BuildConfig.BACKENDLESS_PASSWORD)
        val response = deferred.await()
        return response.userToken
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
    fun loginTest() = runBlocking<Unit> {
        val deferred = service.loginAsync(
                BuildConfig.BACKENDLESS_LOGIN, BuildConfig.BACKENDLESS_PASSWORD)
        val response = deferred.await() //Waits for response
        println(response)
        assertNotNull(response)
    }

    @Test
    fun createUserTest() = runBlocking<Unit> {
        val deferred = service.createUserAsync(
                "li.alan180@hotmail.com", "bruhbruh")
        val response = deferred.await() //Waits for response
        println(response)
        assertNotNull(response)
    }

    @Test
    fun loginAndValidUserTokenTest() = runBlocking<Unit> {
        userToken = userToken ?: retrieveToken()
        val deferred = service.validateUserTokenAsync(userToken!!)
        val response = deferred.await()
        assertTrue(response)
    }

    @Test
    fun postItemTest() = runBlocking<Unit> {
        val item = Item("", "",
                null, 50.01, "Computers", true, "Big Computer",
                "An epic computer")
        val deferred = service.postItemAsync(item)
        val response = deferred.await()
        assertNotNull(response)
    }

    @Test
    fun updateItemTest() = runBlocking<Unit> {
        val item = Item("", "", null, 3.14, "Smartphones",
                true, "Nokia Phone", "A brick")
        val updateResponse = service.postItemAsync(item).await()

        val updatedItem = (updateResponse as Item).copy(price = Random.nextDouble(100.0))
        val deferred = service.updateItemAsync(updatedItem)
        val response = deferred.await()
        assertNotNull(response)
    }

    @Test
    fun getDatabaseTest() = runBlocking<Unit> {
        val deferred = service.getItemsAsync(10, 0, null)
        val response = deferred.await()
        println(response)
        assertNotNull(response)
    }

    @Test
    fun getItemTest() = runBlocking<Unit> {
        val item = Item("", "", null, 700.21,
                "Large Household Appliances", true,
                "Smartfridge", "Working smartfridge, touchscreen cracked")
        val updateResponse = service.postItemAsync(item).await()

        val requestObject = updateResponse
        val deferred = service.getItemAsync(requestObject.objectId)
        val response = deferred.await()
        println(response)
        assertNotNull(response)
    }

    @Test
    fun deleteItemTest() = runBlocking<Unit> {
        val item: Item = Item("", "", null, 1337.0,
                "Lighting", true, "Limited Edition Lightbulb",
                "Limited edition lightbulb signed by Thomas Edison himself, no longer working")
        val updateResponse = service.postItemAsync(item).await()

        val requestedObject = updateResponse
        val deferred = service.deleteItemAsync(requestedObject.objectId)
        val response = deferred.await()
        println(response)
        assertNotNull(response)
    }
}