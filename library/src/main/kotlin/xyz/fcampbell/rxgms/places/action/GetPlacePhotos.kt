package xyz.fcampbell.rxgms.location.action.location

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.PlacePhotoMetadataResult
import com.google.android.gms.location.places.Places
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetPlacePhotos(
        apiClient: GoogleApiClient,
        placeId: String
) : PendingResultOnSubscribe<PlacePhotoMetadataResult>(
        Places.GeoDataApi.getPlacePhotos(apiClient, placeId)
)