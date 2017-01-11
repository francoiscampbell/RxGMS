package xyz.fcampbell.rxgms.common

import android.content.Context
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
        private val context: Context,
        private val api: ApiDescriptor<O>
) {
    private var currentSubscription: Subscription = Subscriptions.unsubscribed()
    private var currentApiClient: Observable<GoogleApiClient>? = null

    val apiClient: Observable<GoogleApiClient>
        get() {
            var localRxApiClient = currentApiClient
            if (localRxApiClient != null && !currentSubscription.isUnsubscribed) return localRxApiClient

            localRxApiClient = Observable.create(GoogleApiClientOnSubscribe(context, api))
                    .replay(1)
                    .autoConnect(1) { subscription ->
                        currentSubscription = subscription //to unsub from main client and disconnect from GMS
                    }
                    .first() //to force a terminal event to subscribers after one GoogleApiClient emission
            currentApiClient = localRxApiClient
            return localRxApiClient
        }

    fun disconnect() {
        currentSubscription.unsubscribe()
        currentApiClient = null
    }
}
