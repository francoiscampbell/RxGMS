package xyz.fcampbell.rxgms.common

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2017-01-30.
 */
abstract class RxWrappedAuxiliary<out O>(
        protected val apiClient: Observable<GoogleApiClient>,
        val original: O
) {
    fun <R : Result> fromPendingResult(func: O.(GoogleApiClient) -> PendingResult<R>): Observable<R> {
        return apiClient.flatMap { original.func(it).toObservable() }
    }
}