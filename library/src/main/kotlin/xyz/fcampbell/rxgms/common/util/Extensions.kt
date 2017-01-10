package xyz.fcampbell.rxgms.common.util

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import rx.Observable
import rx.Single
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2017-01-09.
 */
inline fun <T, R> Observable<T>.flatMapSingle(crossinline func: (T) -> Single<R>): Single<R> {
    return flatMap { func(it).toObservable() }.first().toSingle()
}

inline fun <T, R> Observable<T>.mapSingle(crossinline func: (T) -> R): Single<R> {
    return flatMapSingle { Single.just(func(it)) }
}

inline fun <T, R : Result> Observable<T>.pendingResultToSingle(crossinline func: (T) -> PendingResult<R>): Single<R> {
    return flatMapSingle { func(it).toSingle() }
}

fun <T : Result> PendingResult<T>.toSingle(): Single<T> {
    return Single.create(PendingResultOnSubscribe(this))
}