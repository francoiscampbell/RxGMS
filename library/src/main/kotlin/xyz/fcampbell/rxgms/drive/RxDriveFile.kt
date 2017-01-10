package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.DriveFile
import rx.Single
import xyz.fcampbell.rxgms.common.util.toSingle

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxDriveFile(
        googleApiClient: GoogleApiClient,
        val driveFile: DriveFile
) : RxDriveResource(googleApiClient, driveFile) {
    fun open(mode: Int, progressListener: DriveFile.DownloadProgressListener): Single<DriveApi.DriveContentsResult> {
        return driveFile.open(googleApiClient, mode, progressListener).toSingle()
    }
}