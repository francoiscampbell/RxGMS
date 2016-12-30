package xyz.fcampbell.rxgms.location.action.location

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.location.places.PlacePhotoResult
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetPlacePhoto(
        apiClient: GoogleApiClient,
        placePhotoMetadata: PlacePhotoMetadata
) : PendingResultOnSubscribe<PlacePhotoResult>(
        placePhotoMetadata.getPhoto(apiClient)
)