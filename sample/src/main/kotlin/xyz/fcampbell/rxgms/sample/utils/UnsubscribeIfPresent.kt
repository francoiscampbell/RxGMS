package xyz.fcampbell.rxgms.sample.utils

import rx.Subscription

object UnsubscribeIfPresent {
    @JvmStatic
    fun unsubscribe(subscription: Subscription?) {
        subscription?.unsubscribe()
    }
}//no instance
