package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.DriveContents
import com.google.android.gms.drive.ExecutionOptions
import com.google.android.gms.drive.MetadataChangeSet
import rx.Observable
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxDriveContents(
        private val googleApiClient: GoogleApiClient,
        val driveContents: DriveContents
) {
    fun reopenForWrite(): Observable<RxDriveContents> {
        return driveContents.reopenForWrite(googleApiClient)
                .toObservable()
                .map { RxDriveContents(googleApiClient, it.driveContents) }
    }

    fun commit(changeSet: MetadataChangeSet): Observable<Status> {
        return driveContents.commit(googleApiClient, changeSet).toObservable()
    }

    fun commit(changeSet: MetadataChangeSet, executionOptions: ExecutionOptions): Observable<Status> {
        return driveContents.commit(googleApiClient, changeSet, executionOptions).toObservable()
    }
}