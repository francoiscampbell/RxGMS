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
internal class NewDriveContentsOnSubscribe(
        ctx: Context
) : BaseDriveOnSubscribe<DriveApi.DriveContentsResult>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in DriveApi.DriveContentsResult>) {
        val pendingResult = Drive.DriveApi.newDriveContents(apiClient)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}