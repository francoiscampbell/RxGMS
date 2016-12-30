package xyz.fcampbell.rxgms.location.action.geofence

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

internal class RemoveGeofenceRequestIds(
        apiClient: GoogleApiClient,
        geofenceRequestIds: List<String>
) : PendingResultOnSubscribe<Status>(LocationServices.GeofencingApi.removeGeofences(apiClient, geofenceRequestIds))
