package xyz.fcampbell.rxgms.drive.onsubscribe

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveApi
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-24.
 */
internal class FetchDriveIdOnSubscribe(
        private val ctx: Context,
        private val s: String
) : BaseDriveOnSubscribe<DriveApi.DriveIdResult>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in DriveApi.DriveIdResult>) {
        val pendingResult = Drive.DriveApi.fetchDriveId(apiClient, s)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}