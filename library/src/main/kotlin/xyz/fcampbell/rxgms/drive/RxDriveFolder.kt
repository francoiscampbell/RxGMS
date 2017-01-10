package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.*
import com.google.android.gms.drive.query.Query
import rx.Single
import xyz.fcampbell.rxgms.common.util.toSingle

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxDriveFolder(
        googleApiClient: GoogleApiClient,
        val driveFolder: DriveFolder
) : RxDriveResource(googleApiClient, driveFolder) {
    fun listChildren(): Single<DriveApi.MetadataBufferResult> {
        return driveFolder.listChildren(googleApiClient).toSingle()
    }

    fun queryChildren(query: Query): Single<DriveApi.MetadataBufferResult> {
        return driveFolder.queryChildren(googleApiClient, query).toSingle()
    }

    fun createFile(changeSet: MetadataChangeSet, driveContents: DriveContents): Single<DriveFolder.DriveFileResult> {
        return driveFolder.createFile(googleApiClient, changeSet, driveContents).toSingle()
    }

    fun createFile(changeSet: MetadataChangeSet, driveContents: DriveContents, executionOptions: ExecutionOptions): Single<DriveFolder.DriveFileResult> {
        return driveFolder.createFile(googleApiClient, changeSet, driveContents, executionOptions).toSingle()
    }

    fun createFolder(changeSet: MetadataChangeSet): Single<DriveFolder.DriveFolderResult> {
        return driveFolder.createFolder(googleApiClient, changeSet).toSingle()
    }
}