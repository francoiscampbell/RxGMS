package xyz.fcampbell.rxgms.cast

import android.content.Context
import android.view.Display
import com.google.android.gms.cast.CastRemoteDisplay
import com.google.android.gms.common.api.Scope
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxCastRemoteDisplay private constructor() {
    class CastRemoteDisplayApi(
            apiClientDescriptor: ApiClientDescriptor,
            castRemoteDisplayOptions: CastRemoteDisplay.CastRemoteDisplayOptions,
            vararg scopes: Scope
    ) : RxGmsApi<CastRemoteDisplay.CastRemoteDisplayOptions>(
            apiClientDescriptor,
            ApiDescriptor(CastRemoteDisplay.API, castRemoteDisplayOptions, *scopes)
    ) {
        constructor(
                context: Context,
                castRemoteDisplayOptions: CastRemoteDisplay.CastRemoteDisplayOptions,
                vararg scopes: Scope
        ) : this(ApiClientDescriptor(context), castRemoteDisplayOptions, *scopes)

        fun startRemoteDisplay(applicationId: String): Observable<Display> {
            return apiClient.pendingResultToObservable { CastRemoteDisplay.CastRemoteDisplayApi.startRemoteDisplay(it, applicationId) }
                    .map { it.presentationDisplay }
        }

        fun stopRemoteDisplay(): Observable<Display> {
            return apiClient.pendingResultToObservable { CastRemoteDisplay.CastRemoteDisplayApi.stopRemoteDisplay(it) }
                    .map { it.presentationDisplay }
        }
    }
}