package xyz.fcampbell.rxplayservices.sample.location

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.android.gms.location.GeofencingEvent
import xyz.fcampbell.rxplayservices.sample.R

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent)
        val transition = mapTransition(event.geofenceTransition)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Geofence action")
                .setContentText(transition)
                .setTicker("Geofence action")
                .build()
        nm.notify(0, notification)
    }

    private fun mapTransition(event: Int): String {
        when (event) {
            com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER -> return "ENTER"
            com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT -> return "EXIT"
            else -> return "UNKNOWN"
        }
    }
}
