package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.DriveFile
import rx.Observable
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxDriveFile(
        googleApiClient: GoogleApiClient,
        val driveFile: DriveFile
) : RxDriveResource(googleApiClient, driveFile) {
    fun open(mode: Int, progressListener: DriveFile.DownloadProgressListener): Observable<RxDriveContents> {
        return driveFile.open(googleApiClient, mode, progressListener)
                .toObservable()
                .map { RxDriveContents(googleApiClient, it.driveContents) }
    }
}