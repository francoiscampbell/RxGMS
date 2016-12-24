package xyz.fcampbell.rxgms.drive.onsubscribe

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveFolder
import rx.Observer

/**
 * Created by francois on 2016-12-24.
 */
internal class GetRootFolderOnSubscribe(
        ctx: Context
) : BaseDriveOnSubscribe<DriveFolder>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in DriveFolder>) {
        observer.onNext(Drive.DriveApi.getRootFolder(apiClient))
        observer.onCompleted()
    }
}