package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.DriveId
import rx.Observable

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxDriveId(
        private val googleApiClient: GoogleApiClient,
        val driveId: DriveId
) {
    fun asDriveFile(): Observable<RxDriveFile> {
        return Observable.just(RxDriveFile(googleApiClient, driveId.asDriveFile()))
    }

    fun asDriveFolder(): Observable<RxDriveFolder> {
        return Observable.just(RxDriveFolder(googleApiClient, driveId.asDriveFolder()))
    }

    fun asDriveResource(): Observable<RxDriveResource> {
        return Observable.just(RxDriveResource(googleApiClient, driveId.asDriveResource()))
    }
}