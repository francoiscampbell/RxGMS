package xyz.fcampbell.rxgms.location.action.location

import android.app.PendingIntent
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

internal class AddLocationIntentUpdates(
        apiClient: GoogleApiClient,
        locationRequest: LocationRequest,
        pendingIntent: PendingIntent
) : PendingResultOnSubscribe<Status>(
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, pendingIntent)
)
