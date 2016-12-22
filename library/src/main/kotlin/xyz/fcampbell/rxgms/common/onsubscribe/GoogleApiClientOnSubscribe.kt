package xyz.fcampbell.rxgms.common.onsubscribe

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import rx.Observable
import rx.Observer

internal class GoogleApiClientOnSubscribe @SafeVarargs private constructor(
        ctx: Context,
        vararg apis: Api<out Api.ApiOptions.NotRequiredOptions>
) : BaseOnSubscribe<GoogleApiClient>(ctx, *apis) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in GoogleApiClient>) {
        observer.onNext(apiClient)
        observer.onCompleted()
    }

    companion object {
        @SafeVarargs
        @JvmStatic
        fun create(context: Context, vararg apis: Api<out Api.ApiOptions.NotRequiredOptions>): Observable<GoogleApiClient> {
            return Observable.create(GoogleApiClientOnSubscribe(context, *apis))
        }
    }
}
