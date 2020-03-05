package project.ucsd.reee_waste

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import project.ucsd.reee_waste.service.BackendlessResponse
import project.ucsd.reee_waste.service.RwService
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class RwServiceTest {
    val APP_ID: String = "5AC99F5F-F948-44F9-A925-345BCF8DE90B"
    val REST_API_KEY: String = "138246C6-FA44-4BEE-BD10-EADC26888365"

    private lateinit var service: RwService

    @BeforeTest
    fun setupService() {
        service = RwService(APP_ID,
                REST_API_KEY)
    }

    @AfterTest
    fun clearService() {
        service.close()
    }

    @Test
    fun loginTest() = runBlocking<Unit> {
        val deferred: Deferred<BackendlessResponse> = service.loginAsync("cschow@ucsd.edu",
                "yeetBOI1337")
        val response: BackendlessResponse = deferred.await() //Waits for response
        println(response)
        assertNotNull(response)
    }

    @Test
    fun registerTest() = runBlocking<Unit>{
        val deferred: Deferred<BackendlessResponse> = service.createUserAsync(
                "li.alan180@hotmail.com", "bruhbruh")
        val response: BackendlessResponse = deferred.await() //Waits for response
        println(response)
        assertNotNull(response)
    }
}