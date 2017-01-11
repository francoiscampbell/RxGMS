package xyz.fcampbell.rxgms.location

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionResult
import rx.AsyncEmitter
import rx.Observable
import xyz.fcampbell.rxgms.activityrecognition.action.ActivityUpdates
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Reactive way to access Google Play Location APIs
 */
@Suppress("unused")
class RxActivityRecognitionApi internal constructor(
        context: Context
) : RxGmsApi<Api.ApiOptions.NoOptions>(
        context,
        ApiDescriptor(arrayOf(ApiDescriptor.OptionsHolder(ActivityRecognition.API)))
) {
    /**
     * Observable that can be used to observe activity provided by Actity Recognition mechanism.

     * @param detectionIntervalMilliseconds detection interval
     * *
     * @return observable that provides activity recognition
     */
    fun requestActivityUpdates(detectionIntervalMilliseconds: Int): Observable<ActivityRecognitionResult> {
        return apiClient.flatMap {
            Observable.fromEmitter(
                    ActivityUpdates(it, detectionIntervalMilliseconds),
                    AsyncEmitter.BackpressureMode.LATEST)
        }
    }
}