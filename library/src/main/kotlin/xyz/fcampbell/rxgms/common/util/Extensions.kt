package xyz.fcampbell.rxgms.common.util

import rx.Observable
import rx.Single

/**
 * Created by francois on 2017-01-09.
 */
inline fun <T, R> Observable<T>.mapSingle(crossinline func: (T) -> R): Single<R> {
    return flatMapSingle { Single.just(func(it)) }
}

inline fun <T, R> Observable<T>.flatMapSingle(crossinline func: (T) -> Single<R>): Single<R> {
    return flatMap { func(it).toObservable() }.first().toSingle()
}