package xyz.fcampbell.rxgms.sample

import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.activity_places.*
import rx.Observable
import rx.subscriptions.CompositeSubscription
import xyz.fcampbell.rxgms.RxGms
import java.util.*
import java.util.concurrent.TimeUnit

class PlacesActivity : BaseActivity() {
    private val rxGms = RxGms(this)

    private val compositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        place_suggestions_list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val info = parent.adapter.getItem(position) as AutocompleteInfo
            startActivity(PlacesResultActivity.getStartIntent(this@PlacesActivity, info.id))
        }
    }

    override fun onLocationPermissionGranted() {
        compositeSubscription.add(
                rxGms.placesApi.
                        getCurrentPlace(null)
                        .subscribe { buffer ->
                            val likelihood = buffer.get(0)
                            if (likelihood != null) {
                                current_place_view.text = likelihood.place.name
                            }
                            buffer.release()
                        }
        )

        val queryObservable = RxTextView
                .textChanges(place_query_view)
                .map { charSequence -> charSequence.toString() }
                .debounce(1, TimeUnit.SECONDS)
                .filter { s -> !TextUtils.isEmpty(s) }
        val lastKnownLocationObservable = rxGms.locationApi.getLastLocation().toObservable()
        val suggestionsObservable = Observable
                .combineLatest(queryObservable, lastKnownLocationObservable) { query, currentLocation ->
                    QueryWithCurrentLocation(query, currentLocation)
                }
                .flatMap { query ->
                    if (query.location == null) return@flatMap Observable.empty<AutocompletePredictionBuffer>()

                    val latitude = query.location.latitude
                    val longitude = query.location.longitude
                    val bounds = LatLngBounds(
                            LatLng(latitude - 0.05, longitude - 0.05),
                            LatLng(latitude + 0.05, longitude + 0.05)
                    )
                    return@flatMap rxGms.placesApi.getPlaceAutocompletePredictions(query.query, bounds, null).toObservable()
                }

        compositeSubscription.add(suggestionsObservable.subscribe { buffer ->
            val infos = buffer.mapTo(ArrayList<AutocompleteInfo>()) { AutocompleteInfo(it.getFullText(null).toString(), it.placeId ?: "") }
            buffer.release()
            place_suggestions_list.adapter = ArrayAdapter(this@PlacesActivity, android.R.layout.simple_list_item_1, infos)
        })
    }

    override fun onStop() {
        super.onStop()
        compositeSubscription.clear()
    }

    private class QueryWithCurrentLocation(val query: String, val location: Location?)

    private class AutocompleteInfo(private val description: String, val id: String) {
        override fun toString(): String {
            return description
        }
    }
}
