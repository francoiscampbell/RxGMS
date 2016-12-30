package xyz.fcampbell.rxgms.location.action.geofence

import android.app.PendingIntent
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

internal class RemoveGeofenceByPendingIntent(
        apiClient: GoogleApiClient,
        pendingIntent: PendingIntent
) : PendingResultOnSubscribe<Status>(LocationServices.GeofencingApi.removeGeofences(apiClient, pendingIntent))
