package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.*
import com.google.android.gms.drive.query.Query
import rx.Single
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.mapSingle
import xyz.fcampbell.rxgms.common.util.pendingResultToSingle

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
    fun fetchDriveId(resourceId: String): Single<DriveApi.DriveIdResult> {
        return rxApiClient.pendingResultToSingle { Drive.DriveApi.fetchDriveId(it, resourceId) }
    }

    fun getAppFolder(): Single<DriveFolder> {
        return rxApiClient.mapSingle { Drive.DriveApi.getAppFolder(it) }
    }

    fun getRootFolder(): Single<DriveFolder> {
        return rxApiClient.mapSingle { Drive.DriveApi.getRootFolder(it) }
    }

    fun newCreateFileActivityBuilder(): Single<CreateFileActivityBuilder> {
        return Single.just(Drive.DriveApi.newCreateFileActivityBuilder())
    }

    fun newDriveContents(): Single<DriveApi.DriveContentsResult> {
        return rxApiClient.pendingResultToSingle { Drive.DriveApi.newDriveContents(it) }
    }

    fun newOpenFileActivityBuilder(): Single<OpenFileActivityBuilder> {
        return Single.just(Drive.DriveApi.newOpenFileActivityBuilder())
    }

    fun query(query: Query): Single<DriveApi.MetadataBufferResult> {
        return rxApiClient.pendingResultToSingle { Drive.DriveApi.query(it, query) }
    }

    fun requestSync(): Single<Status> {
        return rxApiClient.pendingResultToSingle { Drive.DriveApi.requestSync(it) }
    }

    fun getFileUploadPreferences(): Single<DrivePreferencesApi.FileUploadPreferencesResult> {
        return rxApiClient.pendingResultToSingle { Drive.DrivePreferencesApi.getFileUploadPreferences(it) }
    }

    fun setFileUploadPreferences(fileUploadPreferences: FileUploadPreferences): Single<Status> {
        return rxApiClient.pendingResultToSingle { Drive.DrivePreferencesApi.setFileUploadPreferences(it, fileUploadPreferences) }
    }
}