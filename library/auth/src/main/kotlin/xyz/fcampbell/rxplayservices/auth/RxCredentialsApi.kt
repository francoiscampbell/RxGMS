package xyz.fcampbell.rxplayservices.auth

import android.app.PendingIntent
import android.content.Context
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Auth.CredentialsApi]
 */
@Suppress("unused")
class RxCredentialsApi(
        apiClientDescriptor: ApiClientDescriptor,
        credentialsOptions: Auth.AuthCredentialsOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<CredentialsApi, Auth.AuthCredentialsOptions>(
        apiClientDescriptor,
        ApiDescriptor(Auth.CREDENTIALS_API, Auth.CredentialsApi, credentialsOptions, *scopes)
) {
    constructor(
            context: Context,
            credentialsOptions: Auth.AuthCredentialsOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), credentialsOptions, *scopes)

    fun request(credentialRequest: CredentialRequest): Observable<CredentialRequestResult> {
        return fromPendingResult { request(it, credentialRequest) }
    }

    fun getHintPickerIntent(hintRequest: HintRequest): Observable<PendingIntent> {
        return map { getHintPickerIntent(it, hintRequest) }
    }

    fun save(credential: Credential): Observable<Status> {
        return fromPendingResult { save(it, credential) }
    }

    fun delete(credential: Credential): Observable<Status> {
        return fromPendingResult { delete(it, credential) }
    }

    fun disableAutoSignIn(): Observable<Status> {
        return fromPendingResult { disableAutoSignIn(it) }
    }
}