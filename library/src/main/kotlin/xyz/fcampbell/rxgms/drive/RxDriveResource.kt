package xyz.fcampbell.rxgms.drive

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.DriveId
import com.google.android.gms.drive.DriveResource
import com.google.android.gms.drive.MetadataChangeSet
import com.google.android.gms.drive.events.ChangeListener
import rx.Single
import xyz.fcampbell.rxgms.common.util.toSingle

/**
 * Created by francois on 2017-01-10.
 */
@Suppress("unused")
open class RxDriveResource(
        protected val googleApiClient: GoogleApiClient,
        private val driveResource: DriveResource
) {
    fun getMetadata(): Single<DriveResource.MetadataResult> {
        return driveResource.getMetadata(googleApiClient).toSingle()
    }

    fun updateMetadata(metadataChangeSet: MetadataChangeSet): Single<DriveResource.MetadataResult> {
        return driveResource.updateMetadata(googleApiClient, metadataChangeSet).toSingle()
    }

    fun getDriveId(): Single<DriveId> {
        return Single.just(driveResource.driveId)
    }

    fun listParents(): Single<DriveApi.MetadataBufferResult> {
        return driveResource.listParents(googleApiClient).toSingle()
    }

    fun delete(): Single<Status> {
        return driveResource.delete(googleApiClient).toSingle()
    }

    fun setParents(parents: Set<DriveId>): Single<Status> {
        return driveResource.setParents(googleApiClient, parents).toSingle()
    }

    fun addChangeListener(changeListener: ChangeListener): Single<Status> {
        return driveResource.addChangeListener(googleApiClient, changeListener).toSingle()
    }

    fun removeChangeListener(changeListener: ChangeListener): Single<Status> {
        return driveResource.removeChangeListener(googleApiClient, changeListener).toSingle()
    }

    fun addChangeSubscription(): Single<Status> {
        return driveResource.addChangeSubscription(googleApiClient).toSingle()
    }

    fun removeChangeSubscription(): Single<Status> {
        return driveResource.removeChangeSubscription(googleApiClient).toSingle()
    }

    fun trash(): Single<Status> {
        return driveResource.trash(googleApiClient).toSingle()
    }

    fun untrash(): Single<Status> {
        return driveResource.untrash(googleApiClient).toSingle()
    }
}