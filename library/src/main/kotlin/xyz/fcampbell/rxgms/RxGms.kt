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
class RxGms(private val ctx: Context) {
    val activityRecognitionApi by lazy { RxActivityRecognitionApi(ctx) }
    val driveApi by lazy { RxDriveApi() }
    val locationApi by lazy { RxLocationApi(ctx) }
    val placesApi by lazy { RxPlacesApi(ctx) }
}