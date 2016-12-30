package xyz.fcampbell.rxgms.location.action.geofence

import android.app.PendingIntent
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

internal class AddGeofence(
        apiClient: GoogleApiClient,
        request: GeofencingRequest,
        geofenceTransitionPendingIntent: PendingIntent
) : PendingResultOnSubscribe<Status>(
        LocationServices.GeofencingApi.addGeofences(apiClient, request, geofenceTransitionPendingIntent)
)