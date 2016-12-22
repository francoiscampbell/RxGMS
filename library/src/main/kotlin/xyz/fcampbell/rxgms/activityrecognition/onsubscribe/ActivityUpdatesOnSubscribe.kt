package xyz.fcampbell.rxgms.activityrecognition.onsubscribe

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionResult

import rx.Observable
import rx.Observer
import xyz.fcampbell.android.rxgms.BuildConfig

class ActivityUpdatesOnSubscribe private constructor(
        private val context: Context,
        private val detectionIntervalMilliseconds: Int
) : BaseActivityRecognitionOnSubscribe<ActivityRecognitionResult>(context) {
    private var receiver: ActivityUpdatesBroadcastReceiver? = null

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in ActivityRecognitionResult>) {
        receiver = ActivityUpdatesBroadcastReceiver(observer)
        context.registerReceiver(receiver, IntentFilter(ACTION_ACTIVITY_DETECTED))
        val receiverIntent = receiverPendingIntent
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(apiClient, detectionIntervalMilliseconds.toLong(), receiverIntent)
    }

    private val receiverPendingIntent: PendingIntent
        get() = PendingIntent.getBroadcast(context, 0, Intent(ACTION_ACTIVITY_DETECTED), PendingIntent.FLAG_UPDATE_CURRENT)

    override fun onUnsubscribe(apiClient: GoogleApiClient) {
        if (apiClient.isConnected) {
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(apiClient, receiverPendingIntent)
        }
        context.unregisterReceiver(receiver)
        receiver = null
    }

    private class ActivityUpdatesBroadcastReceiver(private val observer: Observer<in ActivityRecognitionResult>) : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (ActivityRecognitionResult.hasResult(intent)) {
                val result = ActivityRecognitionResult.extractResult(intent)
                observer.onNext(result)
            }
        }
    }

    companion object {
        private val ACTION_ACTIVITY_DETECTED = "${BuildConfig.APPLICATION_ID}.ACTION_ACTIVITY_UPDATE_DETECTED"

        @JvmStatic
        fun createObservable(ctx: Context, detectionIntervalMiliseconds: Int): Observable<ActivityRecognitionResult> {
            return Observable.create(ActivityUpdatesOnSubscribe(ctx, detectionIntervalMiliseconds))
        }
    }
}
