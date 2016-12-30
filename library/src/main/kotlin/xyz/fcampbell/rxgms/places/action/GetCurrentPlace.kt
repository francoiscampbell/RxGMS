package xyz.fcampbell.rxgms.location.action.location

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.PlaceFilter
import com.google.android.gms.location.places.PlaceLikelihoodBuffer
import com.google.android.gms.location.places.Places
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetCurrentPlace(
        apiClient: GoogleApiClient,
        placeFilter: PlaceFilter?
) : PendingResultOnSubscribe<PlaceLikelihoodBuffer>(Places.PlaceDetectionApi.getCurrentPlace(apiClient, placeFilter))