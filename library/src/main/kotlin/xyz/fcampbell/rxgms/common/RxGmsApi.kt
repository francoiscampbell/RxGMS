package xyz.fcampbell.rxgms.common

import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposables
import xyz.fcampbell.rxgms.common.action.GoogleApiClientOnSubscribe
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2016-12-29.
 */
abstract class RxGmsApi<out A, O : Api.ApiOptions>(
        apiClientDescriptor: ApiClientDescriptor,
        private val api: ApiDescriptor<A, O>
) {
    companion object {
        private const val TAG = "RxGmsApi"
    }

    private val googleApiClientOnSubscribe = GoogleApiClientOnSubscribe(apiClientDescriptor, api)

    private var currentDisposable = Disposables.disposed()
    private var currentApiClientPair: Observable<Pair<GoogleApiClient, Bundle?>>? = null

    private val apiClientPair: Observable<Pair<GoogleApiClient, Bundle?>>
        get() {
            var localRxApiClient = currentApiClientPair
            if (localRxApiClient != null && !currentDisposable.isDisposed) return localRxApiClient

            localRxApiClient = Observable.create(googleApiClientOnSubscribe)
                    .doOnSubscribe {
                        Log.d(TAG, "Sub to main apiClient")
                    }
                    .doOnDispose {
                        Log.d(TAG, "Dispose main apiClient")
                    }
                    .replay(1)
                    .autoConnect(1) { disposable ->
                        currentDisposable = disposable //to unsub from main client and disconnect from GMS
                    }
                    .firstElement() //to force a terminal event to subscribers after one GoogleApiClient emission
                    .toObservable()
                    .doOnSubscribe {
                        Log.d(TAG, "Sub to replayed apiClient")
                    }
                    .doOnDispose {
                        Log.d(TAG, "Dispose replayed apiClient")
                    }
            currentApiClientPair = localRxApiClient
            return localRxApiClient
        }

    val apiClient: Observable<GoogleApiClient>
        get() = apiClientPair.map { it.first }

    val bundle: Observable<Bundle?>
        get() = apiClientPair.map { it.second }

    fun disconnect() {
        currentDisposable.dispose()
        currentApiClientPair = null
    }

    fun <R> just(item: R): Observable<R> {
        return Observable.just(item)
    }

    fun <R> create(sourceFunc: (GoogleApiClient) -> ObservableOnSubscribe<R>): Observable<R> {
        return apiClient.flatMap { Observable.create(sourceFunc(it)) }
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

    fun toCompletable(func: A.(GoogleApiClient) -> Unit): Completable {
        return apiClient.map { api.apiInterface.func(it) }.ignoreElements()
    }
}
