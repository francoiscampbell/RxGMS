package xyz.fcampbell.rxplayservices.common.action

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Releasable
import com.google.android.gms.common.api.Result
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import xyz.fcampbell.rxplayservices.common.exception.StatusException

/**
 * Wraps a [PendingResult] and emits its result as an [Observable]. If the [Observable] is canceled or disposed, the [PendingResult] is canceled and any [Releasable] payload is released. If the [PendingResult] was successful, [onNext] is called with the [Result] of the [PendingResult], and if the [PendingResult] was not successful, [onError] is called with a [StatusException] that wraps the status of the [PendingResult].
 *
 * @param R The payload type of the [PendingResult] and the [Observable]'s type parameter
 *
 * @constructor
 * @param action A function that produces a [PendingResult]
 */
internal class PendingResultOnSubscribe<R : Result>(
        private val action: () -> PendingResult<R>
) : ObservableOnSubscribe<R> {

    override fun subscribe(emitter: ObservableEmitter<R>) {
        val pendingResult = action()
        pendingResult.setResultCallback { result ->
            if (result.status.isSuccess) {
                emitter.onNext(result)
                emitter.onComplete()
            } else {
                emitter.onError(StatusException(result.status))
            }
            handleResourceCleanupIfNecessary(result) //TODO watch out for threading (onNext and onComplete may return before processing is done)
        }
        emitter.setCancellable {
            pendingResult.cancel()
        }
    }

    private fun handleResourceCleanupIfNecessary(result: R) {
        when (result) {
            is Releasable -> result.release()
        }
    }
}
