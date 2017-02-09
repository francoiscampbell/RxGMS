package xyz.fcampbell.rxplayservices.panorama

import android.content.Context
import android.net.Uri
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.panorama.Panorama
import com.google.android.gms.panorama.PanoramaApi
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Panorama.PanoramaApi]
 */
@Suppress("unused")
class RxPanoramaApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<PanoramaApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Panorama.API, Panorama.PanoramaApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    fun loadPanoramaInfo(uri: Uri): Observable<PanoramaApi.PanoramaResult> {
        return fromPendingResult { loadPanoramaInfo(it, uri) }
    }

    fun loadPanoramaInfoAndGrantAccess(uri: Uri): Observable<PanoramaApi.PanoramaResult> {
        return fromPendingResult { loadPanoramaInfoAndGrantAccess(it, uri) }
    }
}