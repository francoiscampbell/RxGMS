package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.*
import com.google.android.gms.drive.query.Query
import rx.Single
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.common.util.flatMapSingle
import xyz.fcampbell.rxgms.common.util.mapSingle
import xyz.fcampbell.rxgms.drive.action.*

/**
 * Created by francois on 2016-12-22.
 */
@Suppress("unused")
class RxDriveApi internal constructor(
        context: Context
) : RxGmsApi(context, Drive.API) {
    fun fetchDriveId(resourceId: String): Single<DriveApi.DriveIdResult> {
        return rxApiClient.flatMapSingle { Single.create(FetchDriveId(it, resourceId)) }
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
        return rxApiClient.flatMapSingle { Single.create(NewDriveContents(it)) }
    }

    fun newOpenFileActivityBuilder(): Single<OpenFileActivityBuilder> {
        return Single.just(Drive.DriveApi.newOpenFileActivityBuilder())
    }

    fun query(query: Query): Single<DriveApi.MetadataBufferResult> {
        return rxApiClient.flatMapSingle { Single.create(QueryOnSubscribe(it, query)) }
    }

    fun requestSync(): Single<Status> {
        return rxApiClient.flatMapSingle { Single.create(RequestSync(it)) }
    }

    fun getFileUploadPreferences(): Single<DrivePreferencesApi.FileUploadPreferencesResult> {
        return rxApiClient.flatMapSingle { Single.create(GetFileUploadPreferences(it)) }
    }

    fun setFileUploadPreferences(fileUploadPreferences: FileUploadPreferences): Single<Status> {
        return rxApiClient.flatMapSingle { Single.create(SetFileUploadPreferences(it, fileUploadPreferences)) }
    }
}