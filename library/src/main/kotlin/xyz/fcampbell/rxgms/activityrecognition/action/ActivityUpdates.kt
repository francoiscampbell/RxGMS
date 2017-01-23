package xyz.fcampbell.rxgms.activityrecognition.action

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionResult
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import xyz.fcampbell.rxgms.BuildConfig

internal class ActivityUpdates(
        private val apiClient: GoogleApiClient,
        private val detectionIntervalMilliseconds: Int
) : ObservableOnSubscribe<ActivityRecognitionResult> {
    private val context = apiClient.context
    private var receiver: ActivityUpdatesBroadcastReceiver? = null


    override fun subscribe(emitter: ObservableEmitter<ActivityRecognitionResult>) {
        emitter.setCancellable {
            if (apiClient.isConnected) {
                ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(apiClient, receiverPendingIntent)
            }
            context.unregisterReceiver(receiver)
            receiver = null
        }

        receiver = ActivityUpdatesBroadcastReceiver(emitter)
        context.registerReceiver(receiver, IntentFilter(ACTION_ACTIVITY_DETECTED))
        val receiverIntent = receiverPendingIntent
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(apiClient, detectionIntervalMilliseconds.toLong(), receiverIntent)
    }

    private val receiverPendingIntent: PendingIntent
        get() = PendingIntent.getBroadcast(apiClient.context, 0, Intent(ACTION_ACTIVITY_DETECTED), PendingIntent.FLAG_UPDATE_CURRENT)

    private class ActivityUpdatesBroadcastReceiver(private val emitter: ObservableEmitter<in ActivityRecognitionResult>) : BroadcastReceiver() {
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
