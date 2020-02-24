package project.ucsd.reee_waste

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <T> Deferred<T>.callback(
        callback: (T) -> Unit
): Job = GlobalScope.launch {
    val response = await()
    callback(response)
}