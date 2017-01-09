package xyz.fcampbell.rxgms

import android.content.Context
import com.google.android.gms.common.api.Scope
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
    fun getDriveApi(accountName: String, vararg scopes: Scope) = RxDriveApi(context, accountName, *scopes)
    val locationApi by lazy { RxLocationApi(context) }
    val placesApi by lazy { RxPlacesApi(context) }
}