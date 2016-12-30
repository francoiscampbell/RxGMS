package xyz.fcampbell.rxgms.common

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import rx.Observable
import rx.Single
import rx.Subscription
import rx.schedulers.Schedulers
import xyz.fcampbell.rxgms.common.action.GoogleApiClientOnSubscribe

/**
 * Created by francois on 2016-12-29.
 */
open class RxGmsApi(
        context: Context,
        vararg services: Api<out Api.ApiOptions.NotRequiredOptions>
) {
    private var currentSubscription: Subscription? = null

    protected val rxApiClient: Observable<GoogleApiClient> = Single.create(GoogleApiClientOnSubscribe(context, *services))
            .subscribeOn(Schedulers.io()) //TODO maybe not necessary?
            .toObservable()
            .replay()
            .publish()
            .autoConnect(1, { subscription ->
                currentSubscription = subscription
            })

    fun disconnect() {
        currentSubscription?.unsubscribe()
    }
}
