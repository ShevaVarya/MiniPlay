package io.github.shevavarya.avito_tech_internship.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    shouldCancelPrevious: Boolean = true,
    listener: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null

    return { data: T ->
        if (shouldCancelPrevious && debounceJob?.isActive == true) {
            debounceJob?.cancel()
        }

        if (shouldCancelPrevious || debounceJob?.isCompleted != false) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                listener.invoke(data)
            }
        }
    }
}
