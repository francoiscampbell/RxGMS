package xyz.fcampbell.rxplayservices.drive

import xyz.fcampbell.rxplayservices.base.RxWrappedApi

/**
 * Wraps [DriveContents]
 */
@Suppress("unused")
class RxDriveContents(
        override val apiClient: Observable<GoogleApiClient>,
        override val original: DriveContents
) : RxWrappedApi<DriveContents> {
    fun reopenForWrite(): Observable<RxDriveContents> {
        return fromPendingResult { reopenForWrite(it) }
                .map { RxDriveContents(apiClient, it.driveContents) }
    }

    fun commit(changeSet: MetadataChangeSet): Observable<Status> {
        return fromPendingResult { commit(it, changeSet) }
    }

    fun commit(changeSet: MetadataChangeSet, executionOptions: ExecutionOptions): Observable<Status> {
        return fromPendingResult { commit(it, changeSet, executionOptions) }
    }
}