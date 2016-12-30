package xyz.fcampbell.rxgms

import android.content.Context
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
    val driveApi by lazy { RxDriveApi(context) }
    val locationApi by lazy { RxLocationApi(context) }
    val placesApi by lazy { RxPlacesApi(context) }
}