package xyz.fcampbell.rxplayservices.sample.drive

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.MetadataBuffer
import com.google.android.gms.drive.MetadataChangeSet
import com.google.android.gms.identity.intents.UserAddressRequest
import com.google.android.gms.identity.intents.model.CountrySpecification
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.auth.RxGoogleSignInApi
import xyz.fcampbell.rxplayservices.drive.RxDriveApi
import xyz.fcampbell.rxplayservices.drive.RxDrivePreferencesApi
import xyz.fcampbell.rxplayservices.identity.RxAddress

/**
 * Created by francois on 2017-01-10.
 */
class DriveActivity : AppCompatActivity() {

    private val driveApi = RxDriveApi(this, Drive.SCOPE_FILE, Drive.SCOPE_APPFOLDER)
    private val drivePrefsApi = RxDrivePreferencesApi(this, Drive.SCOPE_FILE, Drive.SCOPE_APPFOLDER)

    private val gso = GoogleSignInOptions.Builder()
            .requestEmail()
            .build()
    private val googleSignInApi = RxGoogleSignInApi(this, gso)

    private val addressApi = RxAddress(this)

    override fun onStart() {
        super.onStart()

        getRootFolder()
//        getGoogleAccount() //todo move to other activity
//        getAddress() //todo move to other activity
    }

    private fun getRootFolder() {
//        drivePrefsApi.getFileUploadPreferences()
//                .map { it.fileUploadPreferences }
//                .subscribe({
//                    Log.d(TAG, "Got file upload prefs: $it")
//                    Log.d(TAG, "batteryUsagePreference: ${it.batteryUsagePreference}")
//                    Log.d(TAG, "isRoamingAllowed: ${it.isRoamingAllowed}")
//                    Log.d(TAG, "networkTypePreference: ${it.networkTypePreference}")
//                }, { throwable ->
//                    Log.d(TAG, "Error", throwable)
//                })

        driveApi.getAppFolder()
                .flatMap { appFolder ->
                    appFolder.listChildren()
                            .map {
                                it.metadataBuffer
                            }
                            .flatMap {
//                                Observable.error<MetadataBuffer>(Exception()) //test
                                if (it.none()) {
                                    Observable.error<MetadataBuffer>(Exception())
                                } else {
                                    Observable.just(it)
                                }
                            }
                            .doOnSubscribe {
                                Log.d(TAG, "Sub to listChildren")
                            }
                            .doOnDispose {
                                Log.d(TAG, "Dispose listChildren")
                            }
                            .retryWhen { errors ->
                                errors.flatMap {
                                    driveApi.newDriveContents()
                                }.flatMap {
                                    val changeSet = MetadataChangeSet.Builder()
                                            .setTitle("CreatedFile")
                                            .setDescription("Created by RxPlayServices sample app")
                                            .build()
                                    appFolder.createFile(changeSet, it.original)
                                }
                            }
                }
                .doOnSubscribe {
                    Log.d(TAG, "Sub to getAppFolder")
                }
                .doOnDispose {
                    Log.d(TAG, "Dispose getAppFolder")
                }
                .subscribe({
                    Log.d(TAG, "Got root folder: $it")
                    it.forEach { Log.d(TAG, "Item: ${it.title}") }
                }, { throwable ->
                    Log.d(TAG, "Error", throwable)
                }, {
                    Log.d(TAG, "getAppFolder onComplete")
                })
    }

    private fun getGoogleAccount() {
        googleSignInApi.signIn()
                .subscribe({ account ->
                    Log.d(TAG, account.email)
                }, { throwable ->
                    Log.d(TAG, "Error: $throwable")
                })
    }

    private fun getAddress() {
        val userAddressRequest = UserAddressRequest.newBuilder()
                .addAllowedCountrySpecification(CountrySpecification("CA"))
                .build()
        addressApi.requestUserAddress(userAddressRequest)
                .subscribe({
                    Log.d(TAG, "onComplete")
                })
    }

    override fun onStop() {
        super.onStop()

        driveApi.disconnect()
    }

    companion object {
        const val TAG = "DriveActivity"
    }
}