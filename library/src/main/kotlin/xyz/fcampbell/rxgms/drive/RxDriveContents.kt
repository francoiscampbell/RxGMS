package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.DriveContents
import com.google.android.gms.drive.ExecutionOptions
import com.google.android.gms.drive.MetadataChangeSet
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.RxWrappedAuxiliary

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxDriveContents(
        apiClient: Observable<GoogleApiClient>,
        driveContents: DriveContents
) : RxWrappedAuxiliary<DriveContents>(apiClient, driveContents) {
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