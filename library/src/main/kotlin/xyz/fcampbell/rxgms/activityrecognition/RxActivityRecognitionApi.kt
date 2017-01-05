package xyz.fcampbell.rxgms.location

import android.content.Context
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionResult
import rx.AsyncEmitter
import rx.Observable
import xyz.fcampbell.rxgms.activityrecognition.action.ActivityUpdates
import xyz.fcampbell.rxgms.common.RxGmsApi

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
    fun requestActivityUpdates(detectionIntervalMilliseconds: Int): Observable<ActivityRecognitionResult> {
        return rxApiClient.flatMap {
            Observable.fromEmitter(
                    ActivityUpdates(it, detectionIntervalMilliseconds),
                    AsyncEmitter.BackpressureMode.LATEST)
        }
    }
}