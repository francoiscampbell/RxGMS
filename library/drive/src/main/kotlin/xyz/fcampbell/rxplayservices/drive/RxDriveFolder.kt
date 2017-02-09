package xyz.fcampbell.rxplayservices.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.*
import com.google.android.gms.drive.query.Query
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.RxWrappedApi

/**
 * Wraps [DriveFolder]
 */
@Suppress("unused")
class RxDriveFolder(
        override val apiClient: Observable<GoogleApiClient>,
        override val original: DriveFolder
) : RxWrappedApi<DriveFolder>,
    RxDriveResource<DriveFolder>(apiClient, original) {

    fun listChildren(): Observable<DriveApi.MetadataBufferResult> {
        return fromPendingResult { listChildren(it) }
    }

    fun queryChildren(query: Query): Observable<DriveApi.MetadataBufferResult> {
        return fromPendingResult { queryChildren(it, query) }
    }

    fun createFile(changeSet: MetadataChangeSet, driveContents: DriveContents): Observable<RxDriveFile> {
        return fromPendingResult { createFile(it, changeSet, driveContents) }
                .map { RxDriveFile(apiClient, it.driveFile) }
    }

    fun createFile(changeSet: MetadataChangeSet, driveContents: DriveContents, executionOptions: ExecutionOptions): Observable<RxDriveFile> {
        return fromPendingResult { createFile(it, changeSet, driveContents, executionOptions) }
                .map { RxDriveFile(apiClient, it.driveFile) }
    }

    fun createFolder(changeSet: MetadataChangeSet): Observable<RxDriveFolder> {
        return fromPendingResult { createFolder(it, changeSet) }
                .map { RxDriveFolder(apiClient, it.driveFolder) }
    }
}