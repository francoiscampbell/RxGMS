package xyz.fcampbell.rxgms.observables

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import rx.Observable
import rx.Subscriber
import rx.subscriptions.Subscriptions

class PendingResultOnSubscribe<T : Result>(
        private val result: PendingResult<T>
) : Observable.OnSubscribe<T> {
    private var complete = false

    override fun call(subscriber: Subscriber<in T>) {
        result.setResultCallback { t ->
            subscriber.onNext(t)
            complete = true
            subscriber.onCompleted()
        }
        subscriber.add(Subscriptions.create {
            if (!complete) {
                result.cancel()
            }
        })
    }
}
