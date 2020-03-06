package project.ucsd.reee_waste

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.backendless.service.BackendlessHttpException
import project.ucsd.reee_waste.backendless.service.RwService

@UnstableDefault
fun <T> RwService.callback(
        deferred: Deferred<T>,
        completion: (Exception?, T?) -> Unit
): Job = client.launch {
    try {
        val result: T = deferred.await()
        completion(null, result)
    } catch (e: BackendlessHttpException) {
        completion(e, null)
    }
}