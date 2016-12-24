package xyz.fcampbell.rxgms.drive.onsubscribe

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.FileUploadPreferences
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-24.
 */
internal class SetFileUploadPreferencesOnSubscribe(
        ctx: Context,
        private val fileUploadPreferences: FileUploadPreferences
) : BaseDriveOnSubscribe<Status>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in Status>) {
        val pendingResult = Drive.DrivePreferencesApi.setFileUploadPreferences(apiClient, fileUploadPreferences)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}