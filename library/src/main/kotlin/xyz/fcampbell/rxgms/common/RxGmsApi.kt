package xyz.fcampbell.rxgms.common

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import rx.Observable
import rx.schedulers.Schedulers
import xyz.fcampbell.rxgms.common.action.GoogleApiClientOnSubscribe

/**
 * Created by francois on 2016-12-29.
 */
open class RxGmsApi(
        context: Context,
        vararg services: Api<out Api.ApiOptions.NotRequiredOptions>
) {
    val rxApiClient: Observable<GoogleApiClient> = Observable.create(GoogleApiClientOnSubscribe(context, *services))
            .subscribeOn(Schedulers.io())
            .replay(1)
            .refCount()
}
