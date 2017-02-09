package xyz.fcampbell.rxplayservices.base.action

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Releasable
import com.google.android.gms.common.api.Result
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import xyz.fcampbell.rxplayservices.base.exception.StatusException

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
        val pendingResult = action() //we want to repeat the action if we re-subscribe
        pendingResult.setResultCallback { result ->
            emitter.setCancellable {
                handleResourceCleanupIfNecessary(result)
            }

            if (result.status.isSuccess) {
                emitter.onNext(result)
                emitter.onComplete()
            } else {
                emitter.onError(StatusException(result.status))
            }
        }
        emitter.setCancellable {
            pendingResult.cancel() //does nothing if called after resultCallback is called
        }
    }

    private fun handleResourceCleanupIfNecessary(result: R) {
        when (result) {
            is Releasable -> result.release()
        }
    }
}