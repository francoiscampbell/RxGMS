package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.DriveContents
import com.google.android.gms.drive.ExecutionOptions
import com.google.android.gms.drive.MetadataChangeSet
import rx.Single
import xyz.fcampbell.rxgms.common.util.toSingle

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxDriveContents(
        private val googleApiClient: GoogleApiClient,
        val driveContents: DriveContents
) {
    fun reopenForWrite(): Single<DriveApi.DriveContentsResult> {
        return driveContents.reopenForWrite(googleApiClient).toSingle()
    }

    fun commit(changeSet: MetadataChangeSet): Single<Status> {
        return driveContents.commit(googleApiClient, changeSet).toSingle()
    }

    fun commit(changeSet: MetadataChangeSet, executionOptions: ExecutionOptions): Single<Status> {
        return driveContents.commit(googleApiClient, changeSet, executionOptions).toSingle()
    }
}