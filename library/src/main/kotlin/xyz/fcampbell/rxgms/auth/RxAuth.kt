package xyz.fcampbell.rxgms.auth

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import rx.AsyncEmitter
import rx.Observable
import xyz.fcampbell.rxgms.auth.exception.SignInException
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.action.GetResultFromEmitter
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable

/**
 * Created by francois on 2017-01-11.
 */
@Suppress("unused")
class RxAuth private constructor() {
    class GoogleSignInApi(
            private val apiClientDescriptor: ApiClientDescriptor,
            googleSignInOptions: GoogleSignInOptions,
            vararg scopes: Scope
    ) : RxGmsApi<GoogleSignInOptions>(
            apiClientDescriptor.setAccountName(""), //don't set account name in GoogleApiClient with Auth API. See https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient.Builder.html#setAccountName(java.lang.String)
            ApiDescriptor(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions, *scopes)
    ) {
        constructor(
                context: Context,
                googleSignInOptions: GoogleSignInOptions,
                vararg scopes: Scope
        ) : this(ApiClientDescriptor(context), googleSignInOptions, *scopes)

        fun getSignInIntent(): Observable<Intent> {
            return apiClient.map { Auth.GoogleSignInApi.getSignInIntent(it) }
        }

        fun signIn(): Observable<GoogleSignInAccount> {
            return getSignInIntent().flatMap { intent ->
                val pendingIntent = PendingIntent.getActivity(apiClientDescriptor.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                val intentSender = pendingIntent.intentSender
                Observable.fromEmitter(
                        GetResultFromEmitter(apiClientDescriptor.context, intentSender),
                        AsyncEmitter.BackpressureMode.LATEST)
                        .map { data -> data ?: throw SignInException("sign-in intent returned was null") }
                        .map { Auth.GoogleSignInApi.getSignInResultFromIntent(it).signInAccount }
            }
        }

        fun silentSignIn(): Observable<GoogleSignInAccount> {
            return apiClient.pendingResultToObservable { Auth.GoogleSignInApi.silentSignIn(it) }
                    .map { it.signInAccount }
        }

        fun revokeAccess(): Observable<Status> {
            return apiClient.pendingResultToObservable { Auth.GoogleSignInApi.revokeAccess(it) }
        }

        fun signOut(): Observable<Status> {
            return apiClient.pendingResultToObservable { Auth.GoogleSignInApi.signOut(it) }
        }
    }

    class CredentialsApi(
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

        fun request(credentialRequest: CredentialRequest): Observable<Credential> {
            return apiClient.pendingResultToObservable { Auth.CredentialsApi.request(it, credentialRequest) }
                    .map { it.credential }
        }

        fun getHintPickerIntent(hintRequest: HintRequest): Observable<PendingIntent> {
            return apiClient.map { Auth.CredentialsApi.getHintPickerIntent(it, hintRequest) }
        }

        fun save(credential: Credential): Observable<Status> {
            return apiClient.pendingResultToObservable { Auth.CredentialsApi.save(it, credential) }
        }

        fun delete(credential: Credential): Observable<Status> {
            return apiClient.pendingResultToObservable { Auth.CredentialsApi.delete(it, credential) }
        }

        fun disableAutoSignIn(): Observable<Status> {
            return apiClient.pendingResultToObservable { Auth.CredentialsApi.disableAutoSignIn(it) }
        }
    }
}