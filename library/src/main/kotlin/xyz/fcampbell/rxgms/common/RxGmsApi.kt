package xyz.fcampbell.rxgms.common

import android.content.Context
import android.util.Log
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import rx.Observable
import rx.Subscription
import rx.subscriptions.Subscriptions
import xyz.fcampbell.rxgms.common.action.GoogleApiClientOnSubscribe

/**
 * Created by francois on 2016-12-29.
 */
open class RxGmsApi<O : Api.ApiOptions>(
        context: Context,
        api: ApiDescriptor<O>
) {
    companion object {
        private const val TAG = "RxGmsApi"
    }

    private var currentSubscription: Subscription = Subscriptions.unsubscribed()

    val rxApiClient: Observable<GoogleApiClient> = Observable.create(GoogleApiClientOnSubscribe(context, api))
            .doOnSubscribe {
                Log.d(TAG, "Sub to main rxApiClient")
            }
            .doOnUnsubscribe {
                Log.d(TAG, "Unsub from main rxApiClient")
            }
//            .subscribeOn(Schedulers.io())
            .replay(1)
            .autoConnect(1) { subscription ->
                currentSubscription = subscription
            } //todo figure this out (vs. refCount)
            .first()
            .doOnSubscribe {
                Log.d(TAG, "Sub to replayed rxApiClient")
            }
            .doOnUnsubscribe {
                Log.d(TAG, "Unsub from replayed rxApiClient")
            }

    fun disconnect() {
        currentSubscription.unsubscribe()
    }
}
