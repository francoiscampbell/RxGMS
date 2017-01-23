package xyz.fcampbell.rxgms.auth

import android.app.PendingIntent
import android.content.Context
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.*
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

@Suppress("unused")
class RxCredentialsApi(
        apiClientDescriptor: ApiClientDescriptor,
        credentialsOptions: Auth.AuthCredentialsOptions,
        vararg scopes: Scope
) : RxGmsApi<CredentialsApi, Auth.AuthCredentialsOptions>(
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