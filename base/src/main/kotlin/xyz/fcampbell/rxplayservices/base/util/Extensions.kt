package xyz.fcampbell.rxplayservices.base.util

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.action.PendingResultOnSubscribe

/**
 * Convenience extension function to convert a [PendingResult] to an [Observable].
 *
 * @param T The type parameter of the [PendingResult] and the resulting [Observable].
 * @receiver The [PendingResult] to transform.
 * @return An [Observable] that emits the [Result] of this [PendingResult] when it is ready.
 */
fun <T : Result> PendingResult<T>.toObservable(): Observable<T> {
    return Observable.create(PendingResultOnSubscribe { this })
}