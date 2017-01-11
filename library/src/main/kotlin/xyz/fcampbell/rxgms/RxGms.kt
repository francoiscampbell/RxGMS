package xyz.fcampbell.rxgms

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import xyz.fcampbell.rxgms.auth.RxAuthApi
import xyz.fcampbell.rxgms.drive.RxDriveApi
import xyz.fcampbell.rxgms.location.RxActivityRecognitionApi
import xyz.fcampbell.rxgms.location.RxLocationApi
import xyz.fcampbell.rxgms.location.RxPlacesApi

/**
 * Factory of observables that can manipulate location
 * delivered by Google Play Services.
 */
class RxGms(private val context: Context) {
    val activityRecognitionApi by lazy { RxActivityRecognitionApi(context) }

    fun getAuthApi(accountName: String, googleSignInOptions: GoogleSignInOptions, vararg scopes: Scope): RxAuthApi {
        return RxAuthApi(context, accountName, googleSignInOptions, *scopes)
    }

    fun getDriveApi(accountName: String, vararg scopes: Scope): RxDriveApi {
        return RxDriveApi(context, accountName, *scopes)
    }

    val locationApi by lazy { RxLocationApi(context) }

    val placesApi by lazy { RxPlacesApi(context) }
}