package xyz.fcampbell.rxgms.common.util

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.util.*

/**
 * Created by francois on 2017-01-10.
 */
class ErrorResolver(
        private val context: Context
) {
    fun startResolution(connectionResult: ConnectionResult, onResolved: () -> Unit) {
        if (!connectionResult.hasResolution()) return

        val randomRequestCode = Random().nextInt()
        registerCallbackReceiver(randomRequestCode, onResolved)
        ShadowActivity.startForResolution(context, randomRequestCode, connectionResult)
    }

    private fun registerCallbackReceiver(requestCode: Int, func: () -> Unit) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(context)
        localBroadcastManager.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                localBroadcastManager.unregisterReceiver(this)

                if (!intent.hasExtra(ShadowActivity.KEY_REQUEST_CODE)) return
                val originalRequestCode = intent.getIntExtra(ShadowActivity.KEY_REQUEST_CODE, -1)
                if (originalRequestCode == requestCode) {
                    if (!intent.hasExtra(ShadowActivity.KEY_RESULT_CODE)) return
                    val resultCode = intent.getIntExtra(ShadowActivity.KEY_RESULT_CODE, -1)
                    if (resultCode == Activity.RESULT_OK) {
                        func()
                    }
                }
            }
        }, IntentFilter(ShadowActivity.ACTION_SHADOW_RESULT + requestCode))
    }

    class ShadowActivity() : Activity() {
        companion object {
            private const val KEY_CONNECTION_RESULT = "connectionResult"

            const val ACTION_SHADOW_RESULT = "shadowResult"
            const val KEY_REQUEST_CODE = "requestCode"
            const val KEY_RESULT_CODE = "resultCode"

            fun startForResolution(context: Context, requestCode: Int, connectionResult: ConnectionResult) {
                context.startActivity(
                        Intent(context, ShadowActivity::class.java)
                                .putExtra(KEY_REQUEST_CODE, requestCode)
                                .putExtra(KEY_CONNECTION_RESULT, connectionResult)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (savedInstanceState == null) {
                handleIntent(intent)
            }
        }

        override fun onNewIntent(intent: Intent) = handleIntent(intent)

        private fun handleIntent(intent: Intent) {
            val requestCode = intent.getIntExtra(KEY_REQUEST_CODE, -1)
            when {
                intent.hasExtra(KEY_CONNECTION_RESULT) -> {
                    val connectionResult = intent.getParcelableExtra<ConnectionResult>(KEY_CONNECTION_RESULT)
                    if (connectionResult.hasResolution()) {
                        connectionResult.startResolutionForResult(this, requestCode)
                    } else {
                        GoogleApiAvailability.getInstance()
                                .getErrorDialog(this, connectionResult.errorCode, requestCode)
                                ?.show()
                    }
                }
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(Intent(ACTION_SHADOW_RESULT + requestCode)
                            .putExtra(KEY_REQUEST_CODE, requestCode)
                            .putExtra(KEY_RESULT_CODE, resultCode))
            finish()
        }
    }
}