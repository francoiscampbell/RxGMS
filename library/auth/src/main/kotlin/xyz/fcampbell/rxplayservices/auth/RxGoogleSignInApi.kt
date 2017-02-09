package xyz.fcampbell.rxplayservices.auth

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import xyz.fcampbell.rxplayservices.auth.exception.SignInException
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi
import xyz.fcampbell.rxplayservices.base.util.ResultActivity

/**
 * Wraps [Auth.GoogleSignInApi]
 */
@Suppress("unused")
class RxGoogleSignInApi(
        private val apiClientDescriptor: ApiClientDescriptor,
        googleSignInOptions: GoogleSignInOptions
) : RxPlayServicesApi<GoogleSignInApi, GoogleSignInOptions>(
        apiClientDescriptor.setAccountName(""), //don't set account name in GoogleApiClient with Auth API. See https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient.Builder.html#setAccountName(java.lang.String)
        ApiDescriptor(Auth.GOOGLE_SIGN_IN_API, Auth.GoogleSignInApi, googleSignInOptions)
) {
    constructor(
            context: Context,
            googleSignInOptions: GoogleSignInOptions
    ) : this(ApiClientDescriptor(context), googleSignInOptions)

    fun getSignInIntent(): Observable<Intent> {
        return map { getSignInIntent(it) }
    }

    fun signIn(): Observable<GoogleSignInAccount> {
        return getSignInIntent().flatMap { intent ->
            val pendingIntent = PendingIntent.getActivity(apiClientDescriptor.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val intentSender = pendingIntent.intentSender
            ResultActivity.getResult(apiClientDescriptor.context, intentSender)
        }
                .switchIfEmpty(Observable.error(SignInException("sign-in intent returned was null")))
                .flatMap { getSignInResultFromIntent(it) }
                .map { it.signInAccount }
    }

    fun silentSignIn(): Observable<GoogleSignInResult> {
        return fromPendingResult { silentSignIn(it) }
    }

    fun revokeAccess(): Observable<Status> {
        return fromPendingResult { revokeAccess(it) }
    }

    fun signOut(): Observable<Status> {
        return fromPendingResult { signOut(it) }
    }

    fun getSignInResultFromIntent(intent: Intent): Observable<GoogleSignInResult> {
        return Observable.just(Auth.GoogleSignInApi.getSignInResultFromIntent(intent)) //quirk because the method has the same signature
    }
}