package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.*
import com.google.android.gms.drive.events.ChangeListener
import rx.Observable
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
open class RxDriveResource(
        protected val googleApiClient: GoogleApiClient,
        val driveResource: DriveResource
) {
    fun getMetadata(): Observable<Metadata> {
        return driveResource.getMetadata(googleApiClient)
                .toObservable()
                .map { it.metadata }
    }

    fun updateMetadata(metadataChangeSet: MetadataChangeSet): Observable<Metadata> {
        return driveResource.updateMetadata(googleApiClient, metadataChangeSet)
                .toObservable()
                .map { it.metadata }
    }

    fun getDriveId(): Observable<DriveId> {
        return Observable.just(driveResource.driveId)
    }

    fun listParents(): Observable<MetadataBuffer> {
        return driveResource.listParents(googleApiClient)
                .toObservable()
                .map { it.metadataBuffer }
    }

    fun delete(): Observable<Status> {
        return driveResource.delete(googleApiClient).toObservable()
    }

    fun setParents(parents: Set<DriveId>): Observable<Status> {
        return driveResource.setParents(googleApiClient, parents).toObservable()
    }

    fun addChangeListener(changeListener: ChangeListener): Observable<Status> {
        return driveResource.addChangeListener(googleApiClient, changeListener).toObservable()
    }

    fun removeChangeListener(changeListener: ChangeListener): Observable<Status> {
        return driveResource.removeChangeListener(googleApiClient, changeListener).toObservable()
    }

    fun addChangeSubscription(): Observable<Status> {
        return driveResource.addChangeSubscription(googleApiClient).toObservable()
    }

    fun removeChangeSubscription(): Observable<Status> {
        return driveResource.removeChangeSubscription(googleApiClient).toObservable()
    }

    fun trash(): Observable<Status> {
        return driveResource.trash(googleApiClient).toObservable()
    }

    fun untrash(): Observable<Status> {
        return driveResource.untrash(googleApiClient).toObservable()
    }
}