package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.DriveId
import rx.Single

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxDriveId(
        private val googleApiClient: GoogleApiClient,
        val driveId: DriveId
) {
    fun asDriveFile(): Single<RxDriveFile> {
        return Single.just(RxDriveFile(googleApiClient, driveId.asDriveFile()))
    }

    fun asDriveFolder(): Single<RxDriveFolder> {
        return Single.just(RxDriveFolder(googleApiClient, driveId.asDriveFolder()))
    }

    fun asDriveResource(): Single<RxDriveResource> {
        return Single.just(RxDriveResource(googleApiClient, driveId.asDriveResource()))
    }
}