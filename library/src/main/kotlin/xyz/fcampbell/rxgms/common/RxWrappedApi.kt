package xyz.fcampbell.rxgms.common

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2017-01-30.
 */
interface RxWrappedApi<out O> {
    val apiClient: Observable<GoogleApiClient>
    val original: O

    fun <R> create(func: O.(GoogleApiClient) -> ObservableOnSubscribe<R>): Observable<R> {
        return apiClient.flatMap { Observable.create(original.func(it)) }
    }

    fun <R> map(func: O.(GoogleApiClient) -> R): Observable<R> {
        return apiClient.map { original.func(it) }
    }

    fun <R> flatMap(func: O.(GoogleApiClient) -> Observable<R>): Observable<R> {
        return apiClient.flatMap { original.func(it) }
    }

    fun <R : Result> fromPendingResult(func: O.(GoogleApiClient) -> PendingResult<R>): Observable<R> {
        return apiClient.flatMap { original.func(it).toObservable() }
    }

    fun toCompletable(func: O.(GoogleApiClient) -> Unit): Completable {
        return apiClient.map { original.func(it) }.ignoreElements()
    }
}