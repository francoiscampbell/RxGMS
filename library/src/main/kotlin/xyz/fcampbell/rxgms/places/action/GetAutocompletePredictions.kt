package xyz.fcampbell.rxgms.location.action.location

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal class GetAutocompletePredictions(
        apiClient: GoogleApiClient,
        query: String,
        bounds: LatLngBounds,
        filter: AutocompleteFilter?
) : PendingResultOnSubscribe<AutocompletePredictionBuffer>(
        Places.GeoDataApi.getAutocompletePredictions(apiClient, query, bounds, filter)
)