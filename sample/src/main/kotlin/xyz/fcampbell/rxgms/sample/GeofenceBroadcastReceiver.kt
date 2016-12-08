package xyz.fcampbell.rxgms.sample

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent)
        val transition = mapTransition(event.geofenceTransition)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context)
                .setSmallIcon(xyz.fcampbell.android.rxgms.sample.R.drawable.ic_launcher)
                .setContentTitle("Geofence action")
                .setContentText(transition)
                .setTicker("Geofence action")
                .build()
        nm.notify(0, notification)
    }

    private fun mapTransition(event: Int): String {
        when (event) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> return "ENTER"
            Geofence.GEOFENCE_TRANSITION_EXIT -> return "EXIT"
            else -> return "UNKNOWN"
        }
    }
}
