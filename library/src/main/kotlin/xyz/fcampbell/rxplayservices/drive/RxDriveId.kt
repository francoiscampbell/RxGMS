package xyz.fcampbell.rxplayservices.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.DriveId
import com.google.android.gms.drive.DriveResource
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.common.RxWrappedApi

/**
 * Wraps [DriveId]
 */
@Suppress("unused")
class RxDriveId(
        override val apiClient: Observable<GoogleApiClient>,
        override val original: DriveId
) : RxWrappedApi<DriveId> {
    fun asDriveFile(): Observable<RxDriveFile> {
        return Observable.just(RxDriveFile(apiClient, original.asDriveFile()))
    }

    fun asDriveFolder(): Observable<RxDriveFolder> {
        return Observable.just(RxDriveFolder(apiClient, original.asDriveFolder()))
    }

    fun asDriveResource(): Observable<RxDriveResource<DriveResource>> {
        return Observable.just(RxDriveResource(apiClient, original.asDriveResource()))
    }
}