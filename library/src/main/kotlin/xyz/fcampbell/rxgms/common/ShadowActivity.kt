package xyz.fcampbell.rxgms.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

/**
 * Created by francois on 2017-01-09.
 */
class ShadowActivity() : Activity() {
    companion object {
        const val REQUEST_CODE_RESOLUTION = 1
        const val KEY_CONNECTION_RESULT = "connectionResult"
        const val ACTION_TRY_RECONNECT = "tryReconnect"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) = handleIntent(intent)

    private fun handleIntent(intent: Intent) {
        val connectionResult = intent.getParcelableExtra<ConnectionResult>(KEY_CONNECTION_RESULT)
        if (connectionResult.hasResolution()) {
            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION)
        } else {
            GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, connectionResult.errorCode, REQUEST_CODE_RESOLUTION)
                    ?.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_RESOLUTION) {
            if (resultCode == RESULT_OK) {
                LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(ACTION_TRY_RECONNECT))
            }
            finish()
        }
    }
}