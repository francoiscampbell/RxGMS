package xyz.fcampbell.rxplayservices.drive

import xyz.fcampbell.rxplayservices.base.RxWrappedApi

/**
 * Wraps [DriveFile]
 */
@Suppress("unused")
class RxDriveFile(
        override val apiClient: Observable<GoogleApiClient>,
        override val original: DriveFile
) : RxWrappedApi<DriveFile>,
    RxDriveResource<DriveFile>(apiClient, original) {

    fun open(mode: Int, progressListener: DriveFile.DownloadProgressListener): Observable<RxDriveContents> {
        return fromPendingResult { open(it, mode, progressListener) }
                .map { RxDriveContents(apiClient, it.driveContents) }
    }
}