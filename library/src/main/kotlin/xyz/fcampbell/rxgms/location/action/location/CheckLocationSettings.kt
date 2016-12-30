package xyz.fcampbell.rxgms.location.action.location

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResult
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class CheckLocationSettings(
        apiClient: GoogleApiClient,
        locationRequest: LocationSettingsRequest
) : PendingResultOnSubscribe<LocationSettingsResult>(LocationServices.SettingsApi.checkLocationSettings(apiClient, locationRequest))