package xyz.fcampbell.rxplayservices.common

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import xyz.fcampbell.rxplayservices.common.util.toObservable

/**
 * Interface to add to a class to denote it as wrapping a Google Play service API. This interface provides convenience functions that reduce the boilerplate required to wrap the API's functions into Rx.
 * @param O The type of the class to wrap.
 */
interface RxWrappedApi<out O> {
    /**
     * The source of the [GoogleApiClient]
     */
    val apiClient: Observable<GoogleApiClient>

    /**
     * The API that implementations of this class wrap. All extension function parameters used in this interface's methods have [original] as their receiver.
     */
    val original: O

    /**
     * Creates a new observable bound to the lifecycle of the [GoogleApiClient].
     * [func] will be called once the [GoogleApiClient] is connected.
     *
     * @param R The returned [Observable]'s type parameter.
     * @param func An extension function that has [original] as its receiver, a [GoogleApiClient] as its parameter, and that returns the [ObservableOnSubscribe] action to be performed upon emission of the GoogleApiClient.
     * @return An [Observable] that executes the output of [func] upon emission of the GoogleApiClient.
     */
    fun <R> create(func: O.(GoogleApiClient) -> ObservableOnSubscribe<R>): Observable<R> {
        return apiClient.flatMap { Observable.create(original.func(it)) }
    }

    /**
     * Applies the Rx map operator to the [GoogleApiClient] in the context of the wrapped API.
     *
     * @param R The returned [Observable]'s type parameter.
     * @param func An extension function that has [original] as its receiver, the [GoogleApiClient] as its parameter, and returns a mapped value.
     * @return An [Observable] that transforms the [GoogleApiClient] into the return value of [func].
     */
    fun <R> map(func: O.(GoogleApiClient) -> R): Observable<R> {
        return apiClient.map { original.func(it) }
    }

    /**
     * Applies the Rx flatMap operator to the [GoogleApiClient] in the context of the wrapped API.
     *
     * @param R The returned [Observable]'s type parameter.
     * @param func An extension function that has [original] as its receiver, the [GoogleApiClient] as its parameter, and returns another [Observable].
     * @return An [Observable] that transforms the [GoogleApiClient] into the return value of [func].
     */
    fun <R> flatMap(func: O.(GoogleApiClient) -> Observable<R>): Observable<R> {
        return apiClient.flatMap { original.func(it) }
    }

    /**
     * Converts a [PendingResult] into an [Observable]. The [PendingResult] is automatically canceled if the [Observable] is cancelled before the [GoogleApiClient] is ready and if the payload of the [PendingResult] is [Releasable], it is automatically disposed.
     *
     * @param R The returned [Observable]'s and the [PendingResult]'s type parameter.
     * @param func An extension function that has [original] as its receiver, the [GoogleApiClient] as its parameter, and returns a [PendingResult].
     * @return An [Observable] that contains the [Result] of the return value of [func].
     */
    fun <R : Result> fromPendingResult(func: O.(GoogleApiClient) -> PendingResult<R>): Observable<R> {
        return apiClient.flatMap { original.func(it).toObservable() }
    }

    /**
     * Converts a function that returns [Unit] or [void] into a [Completable].
     *
     * @param func An extension function that has [original] as its receiver, the [GoogleApiClient] as its parameter, and returns [Unit] or [void].
     * @return A [Completable] that emits [onComplete] when the [GoogleApiClient] is emitted and after [func] is run.
     */
    fun toCompletable(func: O.(GoogleApiClient) -> Unit): Completable {
        return apiClient.map { original.func(it) }.ignoreElements()
    }
}