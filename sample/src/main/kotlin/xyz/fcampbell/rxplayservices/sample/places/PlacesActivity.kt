package xyz.fcampbell.rxplayservices.sample.places

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.widget.AdapterView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_places.*
import xyz.fcampbell.rxplayservices.locationservices.RxFusedLocationApi
import xyz.fcampbell.rxplayservices.places.RxGeoDataApi
import xyz.fcampbell.rxplayservices.places.RxPlaceDetectionApi
import xyz.fcampbell.rxplayservices.sample.PermittedActivity

class PlacesActivity : PermittedActivity() {
    override val permissionsToRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private val fusedLocationApi = RxFusedLocationApi(this)
    private val placeDetectionApi = RxPlaceDetectionApi(this)
    private val geodataApi = RxGeoDataApi(this)

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(xyz.fcampbell.rxplayservices.sample.R.layout.activity_places)
        place_suggestions_list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val info = parent.adapter.getItem(position) as AutocompleteInfo
            startActivity(PlacesResultActivity.getStartIntent(this@PlacesActivity, info.id))
        }
    }


    override fun onPermissionsGranted(vararg permissions: String) {
        if (!permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) return

        compositeDisposable.add(
                placeDetectionApi.
                        getCurrentPlace(null)
                        .subscribe { buffer ->
                            val likelihood = buffer.get(0)
                            if (likelihood != null) {
                                current_place_view.text = likelihood.place.name
                            }
                            buffer.release()
                        }
        )

        //todo convert to RxJava2 when RxBinding2 is released
//        val queryObservable = RxTextView
//                .textChanges(place_query_view)
//                .map { charSequence -> charSequence.toString() }
//                .debounce(1, TimeUnit.SECONDS)
//                .filter { s -> !TextUtils.isEmpty(s) }
//        val lastKnownLocationObservable = fusedLocationApi.getLastLocation()
//        val suggestionsObservable = Observable
//                .combineLatest(queryObservable, lastKnownLocationObservable) { query, currentLocation ->
//                    QueryWithCurrentLocation(query, currentLocation)
//                }
//                .flatMap { query ->
//                    if (query.location == null) return@flatMap Observable.empty<AutocompletePredictionBuffer>()
//
//                    val latitude = query.location.latitude
//                    val longitude = query.location.longitude
//                    val bounds = LatLngBounds(
//                            LatLng(latitude - 0.05, longitude - 0.05),
//                            LatLng(latitude + 0.05, longitude + 0.05)
//                    )
//                    return@flatMap geodataApi.getPlaceAutocompletePredictions(query.query, bounds, null)
//                }
//
//        compositeDisposable.add(suggestionsObservable.subscribe { buffer ->
//            val infos = buffer.mapTo(ArrayList<AutocompleteInfo>()) { AutocompleteInfo(it.getFullText(null).toString(), it.placeId ?: "") }
//            buffer.release()
//            place_suggestions_list.adapter = ArrayAdapter(this@PlacesActivity, android.R.layout.simple_list_item_1, infos)
//        })
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private class QueryWithCurrentLocation(val query: String, val location: Location?)

    private class AutocompleteInfo(private val description: String, val id: String) {
        override fun toString(): String {
            return description
        }
    }
}
