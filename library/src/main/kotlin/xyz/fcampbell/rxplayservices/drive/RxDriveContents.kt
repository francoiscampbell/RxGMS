package xyz.fcampbell.rxplayservices.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.DriveContents
import com.google.android.gms.drive.ExecutionOptions
import com.google.android.gms.drive.MetadataChangeSet
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.common.RxWrappedApi

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