package xyz.fcampbell.rxplayservices.safetynet

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [SafetyNet.SafetyNetApi]
 */
@Suppress("unused")
class RxSafetyNetApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<SafetyNetApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(SafetyNet.API, SafetyNet.SafetyNetApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    fun attest(nonce: ByteArray): Observable<SafetyNetApi.AttestationResult> {
        return fromPendingResult { attest(it, nonce) }
    }

    fun lookupUri(threatTypes: List<Int>, uri: String): Observable<SafetyNetApi.SafeBrowsingResult> {
        return fromPendingResult { lookupUri(it, threatTypes, uri) }
    }

    fun lookupUri(uri: String, vararg threatTypes: Int): Observable<SafetyNetApi.SafeBrowsingResult> {
        return fromPendingResult { lookupUri(it, uri, *threatTypes) }
    }

    fun lookupUriInLocalBlacklist(uri: String, vararg threatTypes: Int): Observable<Boolean> {
        return Observable.just(original.lookupUriInLocalBlacklist(uri, *threatTypes))
    }

    fun isVerifyAppsEnabled(context: Context): Observable<Boolean> {
        return Observable.just(original.isVerifyAppsEnabled(context))
    }
}