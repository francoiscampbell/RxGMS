package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.FileUploadPreferences
import com.google.android.gms.drive.query.Query
import rx.Observable
import xyz.fcampbell.rxgms.drive.onsubscribe.*

/**
 * Created by francois on 2016-12-22.
 */
class RxDriveApi internal constructor(
        private val ctx: Context
) {
    fun fetchDriveId(resourceId: String) = Observable.create(FetchDriveIdOnSubscribe(ctx, resourceId))

    fun getAppFolder() = Observable.create(GetAppFolderOnSubscribe(ctx))

    fun getRootFolder() = Observable.create(GetRootFolderOnSubscribe(ctx))

    fun newCreateFileActivityBuilder() = Drive.DriveApi.newCreateFileActivityBuilder()

    fun newDriveContents() = Observable.create(NewDriveContentsOnSubscribe(ctx))

    fun newOpenFileActivityBuilder() = Drive.DriveApi.newOpenFileActivityBuilder()

    fun query(query: Query) = Observable.create(QueryOnSubscribe(ctx, query))

    fun requestSync() = Observable.create(RequestSyncOnSubscribe(ctx))

    fun getFileUploadPreferences() = Observable.create(GetFileUploadPreferencesOnSubscribe(ctx))

    fun setFileUploadPreferences(fileUploadPreferences: FileUploadPreferences): Observable<Status> {
        return Observable.create(SetFileUploadPreferencesOnSubscribe(ctx, fileUploadPreferences))
    }
}