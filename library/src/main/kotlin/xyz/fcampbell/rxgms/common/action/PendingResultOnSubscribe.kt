package xyz.fcampbell.rxgms.common.action

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Releasable
import com.google.android.gms.common.api.Result
import rx.Observable
import rx.Subscriber
import rx.subscriptions.Subscriptions
import xyz.fcampbell.rxgms.common.exception.StatusException

internal class PendingResultOnSubscribe<R : Result>(
        private val pendingResult: PendingResult<R>
) : Observable.OnSubscribe<R> {

    override fun call(subscriber: Subscriber<in R>) {
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

    private fun handleResourceCleanupIfNecessary(result: R, subscriber: Subscriber<in R>) {
        when (result) {
            is Releasable -> subscriber.add(Subscriptions.create { result.release() })
        }
    }
}
