package xyz.fcampbell.rxgms.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import rx.subscriptions.CompositeSubscription
import xyz.fcampbell.rxgms.sample.R
import xyz.fcampbell.rxgms.RxGms
import xyz.fcampbell.rxgms.sample.utils.UnsubscribeIfPresent

class PlacesResultActivity : BaseActivity() {

    private lateinit var rxGms: RxGms
    private lateinit var compositeSubscription: CompositeSubscription
    private lateinit var placeNameView: TextView
    private lateinit var placeLocationView: TextView
    private lateinit var placeAddressView: TextView
    private var placeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_result)

        placeNameView = findViewById(R.id.place_name_view) as TextView
        placeLocationView = findViewById(R.id.place_location_view) as TextView
        placeAddressView = findViewById(R.id.place_address_view) as TextView

        rxGms = RxGms(this)

        getPlaceIdFromIntent()
    }

    private fun getPlaceIdFromIntent() {
        val loadedIntent = intent
        placeId = loadedIntent.getStringExtra(EXTRA_PLACE_ID)

        if (placeId == null) {
            throw IllegalStateException("You must start SearchResultsActivity with a non-null place Id using getStartIntent(Context, String)")
        }
    }

    override fun onLocationPermissionGranted() {
        compositeSubscription = CompositeSubscription()
        compositeSubscription.add(rxGms.getPlaceById(placeId)
                .subscribe { buffer ->
                    val place = buffer.get(0)
                    if (place != null) {
                        placeNameView.text = place.name
                        placeLocationView.text = place.latLng.latitude.toString() + ", " + place.latLng.longitude
                        placeAddressView.text = place.address
                    }
                    buffer.release()
                })
    }

    override fun onStop() {
        super.onStop()
        UnsubscribeIfPresent.unsubscribe(compositeSubscription)
    }

    companion object {

        private val EXTRA_PLACE_ID = "EXTRA_PLACE_ID"

        fun getStartIntent(context: Context, placeId: String): Intent {
            val startIntent = Intent(context, PlacesResultActivity::class.java)
            startIntent.putExtra(EXTRA_PLACE_ID, placeId)

            return startIntent
        }
    }
}
