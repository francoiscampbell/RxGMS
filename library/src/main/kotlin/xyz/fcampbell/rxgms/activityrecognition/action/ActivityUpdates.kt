package xyz.fcampbell.rxgms.activityrecognition.action

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionResult
import rx.AsyncEmitter
import xyz.fcampbell.android.rxgms.BuildConfig
import xyz.fcampbell.rxgms.common.action.FromEmitter

internal class ActivityUpdates(
        private val apiClient: GoogleApiClient,
        private val detectionIntervalMilliseconds: Int
) : FromEmitter<ActivityRecognitionResult>() {
    private val context = apiClient.context
    private var receiver: ActivityUpdatesBroadcastReceiver? = null

    override fun call(emitter: AsyncEmitter<ActivityRecognitionResult>) {
        super.call(emitter)

        receiver = ActivityUpdatesBroadcastReceiver(emitter)
        context.registerReceiver(receiver, IntentFilter(ACTION_ACTIVITY_DETECTED))
        val receiverIntent = receiverPendingIntent
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(apiClient, detectionIntervalMilliseconds.toLong(), receiverIntent)
    }

    override fun onUnsubscribe() {
        if (apiClient.isConnected) {
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(apiClient, receiverPendingIntent)
        }
        context.unregisterReceiver(receiver)
        receiver = null
    }

    private val receiverPendingIntent: PendingIntent
        get() = PendingIntent.getBroadcast(apiClient.context, 0, Intent(ACTION_ACTIVITY_DETECTED), PendingIntent.FLAG_UPDATE_CURRENT)

    private class ActivityUpdatesBroadcastReceiver(private val emitter: AsyncEmitter<in ActivityRecognitionResult>) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ActivityRecognitionResult.hasResult(intent)) {
                val result = ActivityRecognitionResult.extractResult(intent)
                emitter.onNext(result)
            }
        }
    }

    companion object {
        private val ACTION_ACTIVITY_DETECTED = "${BuildConfig.APPLICATION_ID}.ACTION_ACTIVITY_UPDATE_DETECTED"
    }
}
