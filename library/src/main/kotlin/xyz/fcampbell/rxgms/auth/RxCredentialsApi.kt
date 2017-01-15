package xyz.fcampbell.rxgms.auth

import android.app.PendingIntent
import android.content.Context
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.auth.api.credentials.CredentialRequestResult
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable

@Suppress("unused")
class RxCredentialsApi(
        apiClientDescriptor: ApiClientDescriptor,
        credentialsOptions: Auth.AuthCredentialsOptions,
        vararg scopes: Scope
) : RxGmsApi<Auth.AuthCredentialsOptions>(
        apiClientDescriptor,
        ApiDescriptor(Auth.CREDENTIALS_API, credentialsOptions, *scopes)
) {
    constructor(
            context: Context,
            credentialsOptions: Auth.AuthCredentialsOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), credentialsOptions, *scopes)

    fun request(credentialRequest: CredentialRequest): Observable<CredentialRequestResult> {
        return apiClient.pendingResultToObservable { Auth.CredentialsApi.request(it.first, credentialRequest) }
    }

    fun getHintPickerIntent(hintRequest: HintRequest): Observable<PendingIntent> {
        return apiClient.map { Auth.CredentialsApi.getHintPickerIntent(it.first, hintRequest) }
    }

    fun save(credential: Credential): Observable<Status> {
        return apiClient.pendingResultToObservable { Auth.CredentialsApi.save(it.first, credential) }
    }

    fun delete(credential: Credential): Observable<Status> {
        return apiClient.pendingResultToObservable { Auth.CredentialsApi.delete(it.first, credential) }
    }

    fun disableAutoSignIn(): Observable<Status> {
        return apiClient.pendingResultToObservable { Auth.CredentialsApi.disableAutoSignIn(it.first) }
    }
}