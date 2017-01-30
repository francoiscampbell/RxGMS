package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.DriveId
import com.google.android.gms.drive.DriveResource
import com.google.android.gms.drive.MetadataChangeSet
import com.google.android.gms.drive.events.ChangeListener
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.RxWrappedAuxiliary

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
open class RxDriveResource<out O : DriveResource>(
        apiClient: Observable<GoogleApiClient>,
        driveResource: O
) : RxWrappedAuxiliary<O>(apiClient, driveResource) {
    fun getMetadata(): Observable<DriveResource.MetadataResult> {
        return fromPendingResult { getMetadata(it) }
    }

    fun updateMetadata(metadataChangeSet: MetadataChangeSet): Observable<DriveResource.MetadataResult> {
        return fromPendingResult { updateMetadata(it, metadataChangeSet) }
    }

    fun getDriveId(): Observable<DriveId> {
        return Observable.just(original.driveId)
    }

    fun listParents(): Observable<DriveApi.MetadataBufferResult> {
        return fromPendingResult { listParents(it) }
    }

    fun delete(): Observable<Status> {
        return fromPendingResult { delete(it) }
    }

    fun setParents(parents: Set<DriveId>): Observable<Status> {
        return fromPendingResult { setParents(it, parents) }
    }

    fun addChangeListener(changeListener: ChangeListener): Observable<Status> {
        return fromPendingResult { addChangeListener(it, changeListener) }
    }

    fun removeChangeListener(changeListener: ChangeListener): Observable<Status> {
        return fromPendingResult { removeChangeListener(it, changeListener) }
    }

    fun addChangeSubscription(): Observable<Status> {
        return fromPendingResult { addChangeSubscription(it) }
    }

    fun removeChangeSubscription(): Observable<Status> {
        return fromPendingResult { removeChangeSubscription(it) }
    }

    fun trash(): Observable<Status> {
        return fromPendingResult { trash(it) }
    }

    fun untrash(): Observable<Status> {
        return fromPendingResult { untrash(it) }
    }
}