package project.ucsd.reee_waste

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import project.ucsd.reee_waste.backendless.service.RwService

fun <T> RwService.callback(
        deferred: Deferred<T>,
        completion: (T) -> Unit
): Job = client.launch {
    val result: T = deferred.await()
    completion(result)
}