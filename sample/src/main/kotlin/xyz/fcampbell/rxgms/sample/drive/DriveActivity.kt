package xyz.fcampbell.rxgms.sample.drive

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.drive.Drive
import rx.Subscription
import xyz.fcampbell.rxgms.RxGms

/**
 * Created by francois on 2017-01-10.
 */
class DriveActivity : AppCompatActivity() {
    private var driveContentsSubscription: Subscription? = null

    private val driveApi = RxGms(this).getDriveApi("", Drive.SCOPE_FILE, Drive.SCOPE_APPFOLDER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getRootFolder()
    }

    private fun getRootFolder() {
        driveContentsSubscription = driveApi.getRootFolder()
                .flatMap { it.listChildren() }
                .subscribe({
                    Log.d(TAG, "Got root folder: $it")
                    it.metadataBuffer.forEach { Log.d(TAG, "File: ${it.title}") }
                }, { throwable ->
                    Log.d(TAG, "Error", throwable)
                })
    }

    override fun onStop() {
        super.onStop()

        driveContentsSubscription?.unsubscribe()
    }

    companion object {
        const val TAG = "DriveActivity"
    }
}