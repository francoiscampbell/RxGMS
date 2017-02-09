package xyz.fcampbell.rxplayservices.base.util

import android.app.Activity
import android.content.*
import android.os.Bundle
import xyz.fcampbell.rxplayservices.base.BuildConfig
import java.util.*

/**
 * Transparent [Activity] used to get a result from Google Play services that require user interaction (e.g. [RxGoogleSignInApi.signIn()])
 */
@Suppress("unused")
class ResultActivity : Activity() {
    companion object {
        private const val KEY_INTENT_SENDER = "${BuildConfig.APPLICATION_ID}.intentSender"
        private const val KEY_RESULT_INTENT = "${BuildConfig.APPLICATION_ID}.resultIntent"

        const val ACTION_SHADOW_RESULT = "${BuildConfig.APPLICATION_ID}.shadowResult"
        const val KEY_REQUEST_CODE = "${BuildConfig.APPLICATION_ID}.requestCode"
        const val KEY_RESULT_CODE = "${BuildConfig.APPLICATION_ID}.resultCode"

        fun getResult(context: Context, intentSender: IntentSender): Observable<Intent> {
            val randomRequestCode = Random().nextInt(Int.MAX_VALUE) //positive only
            context.startActivity(
                    Intent(context, ResultActivity::class.java)
                            .putExtra(KEY_REQUEST_CODE, randomRequestCode)
                            .putExtra(KEY_INTENT_SENDER, intentSender)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            return registerCallbackReceiver(context, randomRequestCode)
        }

        private fun registerCallbackReceiver(context: Context, expectedRequestCode: Int): Observable<Intent> {
            val localBroadcastManager = LocalBroadcastManager.getInstance(context)
            return Observable.create { emitter ->
                localBroadcastManager.registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        localBroadcastManager.unregisterReceiver(this)

                        if (!intent.hasExtra(KEY_REQUEST_CODE)) {
                            emitter.onError(ResultException(intent, "returned intent does not have KEY_REQUEST_CODE"))
                            return
                        }

                        val originalRequestCode = intent.getIntExtra(KEY_REQUEST_CODE, -1)
                        if (originalRequestCode != expectedRequestCode) {
                            emitter.onError(ResultException(intent, "originalRequestCode != expectedRequestCode, receiver received wrong broadcast"))
                            return
                        }

                        if (!intent.hasExtra(KEY_RESULT_CODE)) {
                            emitter.onError(ResultException(intent, "returned intent does not have KEY_RESULT_CODE"))
                            return
                        }

                        val resultCode = intent.getIntExtra(KEY_RESULT_CODE, -1)
                        if (resultCode != RESULT_OK) {
                            emitter.onError(ResultException(intent, "resultCode != RESULT_OK"))
                            return
                        }

                        val returnedData = intent.getParcelableExtra<Intent?>(KEY_RESULT_INTENT)
                        if (returnedData != null) emitter.onNext(returnedData)
                        emitter.onComplete()
                    }
                }, IntentFilter(ACTION_SHADOW_RESULT + expectedRequestCode))
            }
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
        if (intent.hasExtra(KEY_INTENT_SENDER)) {
            val intentSender = intent.getParcelableExtra<IntentSender>(KEY_INTENT_SENDER)
            startIntentSenderForResult(intentSender, requestCode, null, 0, 0, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(Intent(ACTION_SHADOW_RESULT + requestCode)
                        .putExtra(KEY_REQUEST_CODE, requestCode)
                        .putExtra(KEY_RESULT_CODE, resultCode)
                        .putExtra(KEY_RESULT_INTENT, data))
        finish()
    }

    class ResultException(val data: Intent, message: String) : Exception(message)
}
