package xyz.fcampbell.rxgms.common.util

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import rx.Observable
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2017-01-09.
 */
inline fun <T, R : Result> Observable<T>.pendingResultToObservable(crossinline func: (T) -> PendingResult<R>): Observable<R> {
    return flatMap { func(it).toObservable() }
}

fun <T : Result> PendingResult<T>.toObservable(): Observable<T> {
    return Observable.create(PendingResultOnSubscribe(this))
}