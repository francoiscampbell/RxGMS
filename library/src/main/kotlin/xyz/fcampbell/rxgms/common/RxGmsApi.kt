package xyz.fcampbell.rxgms.common

import android.os.Bundle
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import rx.AsyncEmitter
import rx.Completable
import rx.Observable
import rx.Subscription
import rx.subscriptions.Subscriptions
import xyz.fcampbell.rxgms.common.action.FromEmitter
import xyz.fcampbell.rxgms.common.action.GoogleApiClientOnSubscribe
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2016-12-29.
 */
abstract class RxGmsApi<out A, O : Api.ApiOptions>(
        apiClientDescriptor: ApiClientDescriptor,
        private val api: ApiDescriptor<A, O>
) {
    private val googleApiClientOnSubscribe = GoogleApiClientOnSubscribe(apiClientDescriptor, api)

    private var currentSubscription: Subscription = Subscriptions.unsubscribed()
    private var currentApiClientPair: Observable<Pair<GoogleApiClient, Bundle?>>? = null

    private val apiClientPair: Observable<Pair<GoogleApiClient, Bundle?>>
        get() {
            var localRxApiClient = currentApiClientPair
            if (localRxApiClient != null && !currentSubscription.isUnsubscribed) return localRxApiClient

            localRxApiClient = Observable.create(googleApiClientOnSubscribe)
                    .replay(1)
                    .autoConnect(1) { subscription ->
                        currentSubscription = subscription //to unsub from main client and disconnect from GMS
                    }
                    .first() //to force a terminal event to subscribers after one GoogleApiClient emission
            currentApiClientPair = localRxApiClient
            return localRxApiClient
        }

    val apiClient: Observable<GoogleApiClient>
        get() = apiClientPair.map { it.first }

    val bundle: Observable<Bundle?>
        get() = apiClientPair.map { it.second }

    fun disconnect() {
        currentSubscription.unsubscribe()
        currentApiClientPair = null
    }

    fun <R> just(item: R): Observable<R> {
        return Observable.just(item)
    }

    fun <R> map(func: A.(GoogleApiClient) -> R): Observable<R> {
        return apiClient.map { api.apiInterface.func(it) }
    }

    fun <R> flatMap(func: A.(GoogleApiClient) -> Observable<R>): Observable<R> {
        return apiClient.flatMap { api.apiInterface.func(it) }
    }

    fun <R : Result> fromPendingResult(func: A.(GoogleApiClient) -> PendingResult<R>): Observable<R> {
        return apiClient.flatMap { api.apiInterface.func(it).toObservable() }
    }

    fun <R> fromEmitter(backpressureMode: AsyncEmitter.BackpressureMode, emitter: (GoogleApiClient) -> FromEmitter<R>): Observable<R> {
        return apiClient.flatMap { Observable.fromEmitter(emitter(it), backpressureMode) }
    }

    fun <R> fromEmitterLatest(emitter: (GoogleApiClient) -> FromEmitter<R>): Observable<R> {
        return fromEmitter(AsyncEmitter.BackpressureMode.LATEST, emitter)
    }

    fun <R> fromEmitterBuffer(emitter: (GoogleApiClient) -> FromEmitter<R>): Observable<R> {
        return fromEmitter(AsyncEmitter.BackpressureMode.BUFFER, emitter)
    }

    fun toCompletable(func: A.(GoogleApiClient) -> Unit): Completable {
        return apiClient.map { api.apiInterface.func(it) }.toCompletable()
    }
}
