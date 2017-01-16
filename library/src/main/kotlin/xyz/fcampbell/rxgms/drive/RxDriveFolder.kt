package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.*
import com.google.android.gms.drive.query.Query
import rx.Observable
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
class RxDriveFolder(
        googleApiClient: GoogleApiClient,
        val driveFolder: DriveFolder
) : RxDriveResource(googleApiClient, driveFolder) {
    fun listChildren(): Observable<DriveApi.MetadataBufferResult> {
        return driveFolder.listChildren(googleApiClient).toObservable()
    }

    fun queryChildren(query: Query): Observable<DriveApi.MetadataBufferResult> {
        return driveFolder.queryChildren(googleApiClient, query).toObservable()
    }

    fun createFile(changeSet: MetadataChangeSet, driveContents: DriveContents): Observable<RxDriveFile> {
        return driveFolder.createFile(googleApiClient, changeSet, driveContents)
                .toObservable()
                .map { RxDriveFile(googleApiClient, it.driveFile) }
    }

    fun createFile(changeSet: MetadataChangeSet, driveContents: DriveContents, executionOptions: ExecutionOptions): Observable<RxDriveFile> {
        return driveFolder.createFile(googleApiClient, changeSet, driveContents, executionOptions)
                .toObservable()
                .map { RxDriveFile(googleApiClient, it.driveFile) }
    }

    fun createFolder(changeSet: MetadataChangeSet): Observable<RxDriveFolder> {
        return driveFolder.createFolder(googleApiClient, changeSet)
                .toObservable()
                .map { RxDriveFolder(googleApiClient, it.driveFolder) }
    }
}