package xyz.fcampbell.rxgms.common.action

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Releasable
import com.google.android.gms.common.api.Result
import rx.Single
import rx.SingleSubscriber
import rx.subscriptions.Subscriptions
import xyz.fcampbell.rxgms.common.exception.StatusException

class PendingResultOnSubscribe<T : Result>(
        private val pendingResult: PendingResult<T>
) : Single.OnSubscribe<T> {
    private var complete = false

    override fun call(subscriber: SingleSubscriber<in T>) {
        pendingResult.setResultCallback { result ->
            if (result.status.isSuccess) {
                handleResourceCleanupIfNecessary(result, subscriber)
                subscriber.onSuccess(result)
                complete = true
            } else {
                subscriber.onError(StatusException(result.status))
            }
        }
        subscriber.add(Subscriptions.create {
            if (!complete) {
                pendingResult.cancel()
            }
        })
    }

    private fun handleResourceCleanupIfNecessary(result: T, subscriber: SingleSubscriber<in T>) {
        when (result) {
            is Releasable -> subscriber.add(Subscriptions.create { result.release() })
        }
    }
}
