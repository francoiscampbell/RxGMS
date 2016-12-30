package xyz.fcampbell.rxgms.location.action.location

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetPlaceById(
        apiClient: GoogleApiClient,
        placeId: String
) : PendingResultOnSubscribe<PlaceBuffer>(
        Places.GeoDataApi.getPlaceById(apiClient, placeId)
)