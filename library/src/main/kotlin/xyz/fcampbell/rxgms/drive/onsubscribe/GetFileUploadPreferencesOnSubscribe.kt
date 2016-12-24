package xyz.fcampbell.rxgms.drive.onsubscribe

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DrivePreferencesApi
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-24.
 */
internal class GetFileUploadPreferencesOnSubscribe(
        ctx: Context
) : BaseDriveOnSubscribe<DrivePreferencesApi.FileUploadPreferencesResult>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in DrivePreferencesApi.FileUploadPreferencesResult>) {
        val pendingResult = Drive.DrivePreferencesApi.getFileUploadPreferences(apiClient)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}