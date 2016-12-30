package xyz.fcampbell.rxgms.location

import android.content.Context
import com.google.android.gms.location.ActivityRecognition
import rx.AsyncEmitter
import rx.Observable
import xyz.fcampbell.rxgms.RxGmsApi
import xyz.fcampbell.rxgms.activityrecognition.action.ActivityUpdates

/**
 * Reactive way to access Google Play Location APIs
 */
class RxActivityRecognitionApi internal constructor(
        context: Context
) : RxGmsApi(context, ActivityRecognition.API) {
    /**
     * Observable that can be used to observe activity provided by Actity Recognition mechanism.

     * @param detectionIntervalMilliseconds detection interval
     * *
     * @return observable that provides activity recognition
     */
    fun requestActivityUpdates(detectionIntervalMilliseconds: Int) = rxApiClient.flatMap {
        Observable.fromEmitter(
                ActivityUpdates(it, detectionIntervalMilliseconds),
                AsyncEmitter.BackpressureMode.LATEST)
    }
}