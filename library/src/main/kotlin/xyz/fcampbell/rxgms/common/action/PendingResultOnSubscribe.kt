package xyz.fcampbell.rxgms.common.action

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Releasable
import com.google.android.gms.common.api.Result
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import xyz.fcampbell.rxgms.common.exception.StatusException

internal class PendingResultOnSubscribe<R : Result>(
        private val pendingResult: PendingResult<R>
) : ObservableOnSubscribe<R> {

    override fun subscribe(emitter: ObservableEmitter<R>) {
        pendingResult.setResultCallback { result ->
            if (result.status.isSuccess) {
                emitter.onNext(result)
                emitter.onComplete()
            } else {
                emitter.onError(StatusException(result.status))
            }
            handleResourceCleanupIfNecessary(result)
        }
        emitter.setCancellable { pendingResult.cancel() }
    }

    private fun handleResourceCleanupIfNecessary(result: R) {
        when (result) {
            is Releasable -> result.release()
        }
    }
}
