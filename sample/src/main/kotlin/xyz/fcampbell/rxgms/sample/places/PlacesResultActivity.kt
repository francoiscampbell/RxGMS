package xyz.fcampbell.rxgms.sample.places

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_places_result.*
import xyz.fcampbell.rxgms.places.RxGeoDataApi
import xyz.fcampbell.rxgms.sample.PermittedActivity
import xyz.fcampbell.rxgms.sample.R

class PlacesResultActivity : PermittedActivity() {
    override val permissionsToRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private val compositeDisposable = CompositeDisposable()
    private var placeId: String? = null

    private val geodataApi = RxGeoDataApi(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_result)

        getPlaceIdFromIntent()
    }

    private fun getPlaceIdFromIntent() {
        val loadedIntent = intent
        placeId = loadedIntent.getStringExtra(EXTRA_PLACE_ID)

        if (placeId == null) {
            throw IllegalStateException("You must start SearchResultsActivity with a non-null place Id using getStartIntent(Context, String)")
        }
    }

    override fun onPermissionsGranted(vararg permissions: String) {
        if (!permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) return

        compositeDisposable.add(geodataApi.getPlaceById(placeId ?: "")
                .subscribe { buffer ->
                    val place = buffer.get(0)
                    if (place != null) {
                        place_name_view.text = place.name
                        place_location_view.text = place.latLng.latitude.toString() + ", " + place.latLng.longitude
                        place_address_view.text = place.address
                    }
                    buffer.release()
                })
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
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
