package xyz.fcampbell.rxgms.sample

import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.jakewharton.rxbinding.widget.RxTextView
import rx.Observable
import rx.functions.Func1
import rx.subscriptions.CompositeSubscription
import xyz.fcampbell.rxgms.ReactiveLocationProvider
import xyz.fcampbell.rxgms.sample.utils.UnsubscribeIfPresent.unsubscribe
import java.util.*
import java.util.concurrent.TimeUnit

class PlacesActivity : BaseActivity() {

    private lateinit var currentPlaceView: TextView
    private lateinit var queryView: EditText
    private lateinit var placeSuggestionsList: ListView
    private lateinit var reactiveLocationProvider: ReactiveLocationProvider
    private lateinit var compositeSubscription: CompositeSubscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        currentPlaceView = findViewById(R.id.current_place_view) as TextView
        queryView = findViewById(R.id.place_query_view) as EditText
        placeSuggestionsList = findViewById(R.id.place_suggestions_list) as ListView
        placeSuggestionsList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val info = parent.adapter.getItem(position) as AutocompleteInfo
            startActivity(PlacesResultActivity.getStartIntent(this@PlacesActivity, info.id))
        }

        reactiveLocationProvider = ReactiveLocationProvider(this)
    }

    override fun onLocationPermissionGranted() {
        compositeSubscription = CompositeSubscription()
        compositeSubscription.add(
                reactiveLocationProvider.getCurrentPlace(null)
                        .subscribe { buffer ->
                            val likelihood = buffer.get(0)
                            if (likelihood != null) {
                                currentPlaceView.text = likelihood.place.name
                            }
                            buffer.release()
                        }
        )

        val queryObservable = RxTextView
                .textChanges(queryView)
                .map { charSequence -> charSequence.toString() }
                .debounce(1, TimeUnit.SECONDS)
                .filter { s -> !TextUtils.isEmpty(s) }
        val lastKnownLocationObservable = reactiveLocationProvider.getLastKnownLocation()
        val suggestionsObservable = Observable
                .combineLatest(queryObservable, lastKnownLocationObservable) { query, currentLocation -> QueryWithCurrentLocation(query, currentLocation) }.flatMap(Func1<QueryWithCurrentLocation, Observable<AutocompletePredictionBuffer>> { q ->
            if (q.location == null) return@Func1 Observable.empty<AutocompletePredictionBuffer>()

            val latitude = q.location.latitude
            val longitude = q.location.longitude
            val bounds = LatLngBounds(
                    LatLng(latitude - 0.05, longitude - 0.05),
                    LatLng(latitude + 0.05, longitude + 0.05)
            )
            reactiveLocationProvider.getPlaceAutocompletePredictions(q.query, bounds, null)
        })

        compositeSubscription.add(suggestionsObservable.subscribe { buffer ->
            val infos = ArrayList<AutocompleteInfo>()
            for (prediction in buffer) {
                infos.add(AutocompleteInfo(prediction.getFullText(null).toString(), prediction.placeId))
            }
            buffer.release()
            placeSuggestionsList.adapter = ArrayAdapter(this@PlacesActivity, android.R.layout.simple_list_item_1, infos)
        })
    }

    override fun onStop() {
        super.onStop()
        unsubscribe(compositeSubscription)
    }

    private class QueryWithCurrentLocation private constructor(val query: String, val location: Location?)

    private class AutocompleteInfo private constructor(private val description: String, private val id: String) {

        override fun toString(): String {
            return description
        }
    }
}
