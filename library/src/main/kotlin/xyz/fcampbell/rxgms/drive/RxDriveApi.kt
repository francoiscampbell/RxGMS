package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.CreateFileActivityBuilder
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveApi.MetadataBufferResult
import com.google.android.gms.drive.OpenFileActivityBuilder
import com.google.android.gms.drive.query.Query
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.fromPendingResult
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2016-12-22.
 */
@Suppress("unused")
class RxDriveApi(
        apiClientDescriptor: ApiClientDescriptor,
        vararg scopes: Scope
) : RxGmsApi<Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Drive.API, null, *scopes)
) {
    constructor(
            context: Context,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), *scopes)

    fun fetchDriveId(resourceId: String): Observable<RxDriveId> {
        return apiClientPair.flatMap { pair ->
            Drive.DriveApi.fetchDriveId(pair.first, resourceId)
                    .toObservable()
                    .map { RxDriveId(pair.first, it.driveId) }
        }
    }

    fun getAppFolder(): Observable<RxDriveFolder> {
        return apiClientPair.map { RxDriveFolder(it.first, Drive.DriveApi.getAppFolder(it.first)) }
    }

    fun getRootFolder(): Observable<RxDriveFolder> {
        return apiClientPair.map { RxDriveFolder(it.first, Drive.DriveApi.getRootFolder(it.first)) }
    }

    fun newCreateFileActivityBuilder(): Observable<CreateFileActivityBuilder> {
        return Observable.just(Drive.DriveApi.newCreateFileActivityBuilder())
    }

    fun newDriveContents(): Observable<RxDriveContents> {
        return apiClientPair.flatMap { pair ->
            Drive.DriveApi.newDriveContents(pair.first)
                    .toObservable()
                    .map { RxDriveContents(pair.first, it.driveContents) }
        }
    }

    fun newOpenFileActivityBuilder(): Observable<OpenFileActivityBuilder> {
        return Observable.just(Drive.DriveApi.newOpenFileActivityBuilder())
    }

    fun query(query: Query): Observable<MetadataBufferResult> {
        return apiClientPair.fromPendingResult { Drive.DriveApi.query(it.first, query) }
    }

    fun requestSync(): Observable<Status> {
        return apiClientPair.fromPendingResult { Drive.DriveApi.requestSync(it.first) }
    }
}