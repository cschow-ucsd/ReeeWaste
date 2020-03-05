package project.ucsd.reee_waste

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import project.ucsd.reee_waste.service.BackendlessResponse
import project.ucsd.reee_waste.service.RwService
import kotlin.test.Test
import kotlin.test.assertNotNull

class RwServiceTest {
    @Test
    fun loginTest() = runBlocking<Unit> {
        val rwInst: RwService = RwService()
        val deferred: Deferred<HttpResponse> = rwInst.loginAsync("cschow@ucsd.edu",
                "yeetBOI1337")
        val response: HttpResponse = deferred.await() //Waits for response
        println(response)
        assertNotNull(response)
    }

    @Test
    fun registerTest() = runBlocking<Unit>{
        val rwInst: RwService = RwService()
        val deferred: Deferred<BackendlessResponse> = rwInst.createUserAsync(
                "li.alan180@hotmail.com", "bruhbruh")
        val response: BackendlessResponse = deferred.await() //Waits for response
        println(response)
        assertNotNull(response)
    }
}