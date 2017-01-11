package xyz.fcampbell.rxgms.auth

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import rx.AsyncEmitter
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.action.GetResultFromEmitter
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2017-01-11.
 */
class RxAuthApi internal constructor(
        private val context: Context,
        accountName: String = "",
        googleSignInOptions: GoogleSignInOptions,
        vararg scopes: Scope
) : RxGmsApi<GoogleSignInOptions>(
        context,
        ApiDescriptor(arrayOf(ApiDescriptor.OptionsHolder(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)), accountName, *scopes)
) {
    fun getSignInIntent(): Observable<Intent> {
        return apiClient.flatMap { Observable.just(Auth.GoogleSignInApi.getSignInIntent(it)) }
    }

    fun signIn(): Observable<GoogleSignInAccount> {
        return apiClient.flatMap {
            val intent = Auth.GoogleSignInApi.getSignInIntent(it)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val intentSender = pendingIntent.intentSender
            Observable.fromEmitter(
                    GetResultFromEmitter(context, intentSender),
                    AsyncEmitter.BackpressureMode.LATEST)
                    .map { Auth.GoogleSignInApi.getSignInResultFromIntent(it).signInAccount }
        }
    }

    fun silentSignIn(): Observable<GoogleSignInAccount> {
        return apiClient.flatMap {
            Auth.GoogleSignInApi.silentSignIn(it)
                    .toObservable()
                    .map { it.signInAccount }
        }
    }

    fun signOut(): Observable<Status> {
        return apiClient.flatMap {
            Auth.GoogleSignInApi.signOut(it)
                    .toObservable()
        }
    }
}