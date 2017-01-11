package xyz.fcampbell.rxgms.sample.drive

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.MetadataBuffer
import com.google.android.gms.drive.MetadataChangeSet
import rx.Observable
import rx.Subscription
import xyz.fcampbell.rxgms.RxGms

/**
 * Created by francois on 2017-01-10.
 */
class DriveActivity : AppCompatActivity() {
    private var fileUploadPrefsSub: Subscription? = null
    private var driveContentsSub: Subscription? = null

    private val driveApi = RxGms(this).getDriveApi("", Drive.SCOPE_FILE, Drive.SCOPE_APPFOLDER)

    override fun onStart() {
        super.onStart()

        getRootFolder()
    }

    private fun getRootFolder() {
//        fileUploadPrefsSub = driveApi.getFileUploadPreferences()
//                .subscribe({
//                    Log.d(TAG, "Got file upload prefs: $it")
//                    Log.d(TAG, "batteryUsagePreference: ${it.batteryUsagePreference}")
//                    Log.d(TAG, "isRoamingAllowed: ${it.isRoamingAllowed}")
//                    Log.d(TAG, "networkTypePreference: ${it.networkTypePreference}")
//                }, { throwable ->
//                    Log.d(TAG, "Error", throwable)
//                })

        driveContentsSub = driveApi.getAppFolder()
                .flatMap { appFolder ->
                    appFolder.listChildren()
                            .flatMap {
                                if (it.none()) {
                                    Observable.error<MetadataBuffer>(Exception())
                                } else {
                                    Observable.just(it)
                                }
                            }
                            .doOnSubscribe {
                                Log.d(TAG, "Sub to listChildren")
                            }
                            .doOnUnsubscribe {
                                Log.d(TAG, "Unsub from listChildren")
                            }
                            .retryWhen { errors ->
                                errors.flatMap {
                                    driveApi.newDriveContents()
                                            .doOnSubscribe {
                                                Log.d(TAG, "Sub to newDriveContents")
                                            }
                                            .doOnUnsubscribe {
                                                Log.d(TAG, "Unsub from newDriveContents")
                                            }
                                }.doOnNext {
                                    val changeSet = MetadataChangeSet.Builder()
                                            .setTitle("CreatedFile")
                                            .setDescription("Created by RxGMS sample app")
                                            .build()
                                    appFolder.createFile(changeSet, it.driveContents)
                                }
                            }
                }
                .doOnSubscribe {
                    Log.d(TAG, "Sub to getAppFolder")
                }
                .doOnUnsubscribe {
                    Log.d(TAG, "Unsub from getAppFolder")
                }
                .subscribe({
                    Log.d(TAG, "Got root folder: $it")
                    it.forEach { Log.d(TAG, "Item: ${it.title}") }
                }, { throwable ->
                    Log.d(TAG, "Error", throwable)
                })
    }

    override fun onStop() {
        super.onStop()

//        fileUploadPrefsSub?.unsubscribe()
//        driveContentsSub?.unsubscribe()
        driveApi.disconnect()
    }

    companion object {
        const val TAG = "DriveActivity"
    }
}