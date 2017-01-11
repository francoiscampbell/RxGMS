package xyz.fcampbell.rxgms.common.action

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Releasable
import com.google.android.gms.common.api.Result
import rx.Observable
import rx.Subscriber
import rx.subscriptions.Subscriptions
import xyz.fcampbell.rxgms.common.exception.StatusException

internal class PendingResultOnSubscribe<T : Result>(
        private val pendingResult: PendingResult<T>
) : Observable.OnSubscribe<T> {

    override fun call(subscriber: Subscriber<in T>) {
        pendingResult.setResultCallback { result ->
            handleResourceCleanupIfNecessary(result, subscriber)
            if (result.status.isSuccess) {
                if (!subscriber.isUnsubscribed) {
                    subscriber.onNext(result)
                    subscriber.onCompleted()
                }
            } else {
                subscriber.onError(StatusException(result.status))
            }
        }
        subscriber.add(Subscriptions.create {
            pendingResult.cancel()
        })
    }

    private fun handleResourceCleanupIfNecessary(result: T, subscriber: Subscriber<in T>) {
        when (result) {
            is Releasable -> subscriber.add(Subscriptions.create { result.release() })
        }
    }
}
