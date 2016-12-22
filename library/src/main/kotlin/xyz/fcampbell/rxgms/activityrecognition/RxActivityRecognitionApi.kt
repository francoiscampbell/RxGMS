package xyz.fcampbell.rxgms.location

import android.content.Context
import com.google.android.gms.location.ActivityRecognitionResult
import rx.Observable
import xyz.fcampbell.rxgms.activityrecognition.onsubscribe.ActivityUpdatesOnSubscribe

/**
 * Reactive way to access Google Play Location APIs
 */
class RxActivityRecognitionApi internal constructor(private val ctx: Context) {
    /**
     * Observable that can be used to observe activity provided by Actity Recognition mechanism.

     * @param detectIntervalMiliseconds detecion interval
     * *
     * @return observable that provides activity recognition
     */
    fun requestActivityUpdates(detectIntervalMiliseconds: Int): Observable<ActivityRecognitionResult> {
        return ActivityUpdatesOnSubscribe.createObservable(ctx, detectIntervalMiliseconds)
    }
}