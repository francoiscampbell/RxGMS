package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.*
import com.google.android.gms.drive.query.Query
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.pendingResultToObservable
import xyz.fcampbell.rxgms.common.util.toObservable

/**
 * Created by francois on 2016-12-22.
 */
@Suppress("unused")
class RxDriveApi internal constructor(
        context: Context,
        accountName: String = "",
        vararg scopes: Scope
) : RxGmsApi<Api.ApiOptions.NoOptions>(
        context,
        ApiDescriptor(arrayOf(ApiDescriptor.OptionsHolder(Drive.API)), accountName, *scopes)
) {
    fun fetchDriveId(resourceId: String): Observable<RxDriveId> {
        return rxApiClient.flatMap { googleApiClient ->
            Drive.DriveApi.fetchDriveId(googleApiClient, resourceId)
                    .toObservable()
                    .map { RxDriveId(googleApiClient, it.driveId) }
        }
    }

    fun getAppFolder(): Observable<RxDriveFolder> {
        return rxApiClient.map { RxDriveFolder(it, Drive.DriveApi.getAppFolder(it)) }
    }

    fun getRootFolder(): Observable<RxDriveFolder> {
        return rxApiClient.map {
            RxDriveFolder(it, Drive.DriveApi.getRootFolder(it))
        }
    }

    fun newCreateFileActivityBuilder(): Observable<CreateFileActivityBuilder> {
        return Observable.just(Drive.DriveApi.newCreateFileActivityBuilder())
    }

    fun newDriveContents(): Observable<RxDriveContents> {
        return rxApiClient.flatMap { googleApiClient ->
            Drive.DriveApi.newDriveContents(googleApiClient)
                    .toObservable()
                    .map {
                        RxDriveContents(googleApiClient, it.driveContents)
                    }
        }
    }

    fun newOpenFileActivityBuilder(): Observable<OpenFileActivityBuilder> {
        return Observable.just(Drive.DriveApi.newOpenFileActivityBuilder())
    }

    fun query(query: Query): Observable<MetadataBuffer> {
        return rxApiClient.pendingResultToObservable { Drive.DriveApi.query(it, query) }
                .map { it.metadataBuffer }
    }

    fun requestSync(): Observable<Status> {
        return rxApiClient.pendingResultToObservable { Drive.DriveApi.requestSync(it) }
    }

    fun getFileUploadPreferences(): Observable<FileUploadPreferences> {
        return rxApiClient.pendingResultToObservable { Drive.DrivePreferencesApi.getFileUploadPreferences(it) }
                .map { it.fileUploadPreferences }
    }

    fun setFileUploadPreferences(fileUploadPreferences: FileUploadPreferences): Observable<Status> {
        return rxApiClient.pendingResultToObservable { Drive.DrivePreferencesApi.setFileUploadPreferences(it, fileUploadPreferences) }
    }
}