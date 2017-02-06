package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.CreateFileActivityBuilder
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.DriveApi.MetadataBufferResult
import com.google.android.gms.drive.OpenFileActivityBuilder
import com.google.android.gms.drive.query.Query
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2016-12-22.
 */
@Suppress("unused")
class RxDriveApi(
        apiClientDescriptor: ApiClientDescriptor,
        vararg scopes: Scope
) : RxGmsApi<DriveApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Drive.API, Drive.DriveApi, null, *scopes)
) {
    constructor(
            context: Context,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), *scopes)

    fun fetchDriveId(resourceId: String): Observable<RxDriveId> {
        return fromPendingResult { fetchDriveId(it, resourceId) }
                .map { RxDriveId(apiClient, it.driveId) }
    }

    fun getAppFolder(): Observable<RxDriveFolder> {
        return map { RxDriveFolder(apiClient, getAppFolder(it)) }
    }

    fun getRootFolder(): Observable<RxDriveFolder> {
        return map { RxDriveFolder(apiClient, getRootFolder(it)) }
    }

    fun newCreateFileActivityBuilder(): Observable<CreateFileActivityBuilder> {
        return Observable.just(Drive.DriveApi.newCreateFileActivityBuilder())
    }

    fun newDriveContents(): Observable<RxDriveContents> {
        return fromPendingResult { newDriveContents(it) }
                .map { RxDriveContents(apiClient, it.driveContents) }
    }

    fun newOpenFileActivityBuilder(): Observable<OpenFileActivityBuilder> {
        return Observable.just(Drive.DriveApi.newOpenFileActivityBuilder())
    }

    fun query(query: Query): Observable<MetadataBufferResult> {
        return fromPendingResult { query(it, query) }
    }

    fun requestSync(): Observable<Status> {
        return fromPendingResult { requestSync(it) }
    }
}