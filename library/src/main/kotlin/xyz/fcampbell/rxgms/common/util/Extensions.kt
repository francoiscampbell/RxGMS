package xyz.fcampbell.rxgms.common.util

import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2017-01-09.
 */
fun <T : Result> PendingResult<T>.toObservable(): Observable<T> {
    return Observable.create(PendingResultOnSubscribe { this })
}

fun <T : Result> (() -> PendingResult<T>).toObservable(): Observable<T> {
    return Observable.create(PendingResultOnSubscribe(this))
}