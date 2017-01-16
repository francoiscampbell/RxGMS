package xyz.fcampbell.rxgms.common.action

import android.support.annotation.CallSuper
import rx.AsyncEmitter
import rx.functions.Action1

/**
 * Created by francois on 2016-12-29.
 */
abstract class FromEmitter<T> : Action1<AsyncEmitter<T>> {

    @CallSuper
    override fun call(emitter: AsyncEmitter<T>) {
        emitter.setCancellation { onUnsubscribe() }
    }

    open fun onUnsubscribe() {
    }
}