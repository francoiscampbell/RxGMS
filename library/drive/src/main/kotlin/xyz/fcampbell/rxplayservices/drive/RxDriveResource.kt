package xyz.fcampbell.rxplayservices.drive

import xyz.fcampbell.rxplayservices.base.RxWrappedApi

/**
 * Wraps [DriveResource]
 */
@Suppress("unused")
open class RxDriveResource<out O : DriveResource>(
        override val apiClient: Observable<GoogleApiClient>,
        override val original: O
) : RxWrappedApi<O> {
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