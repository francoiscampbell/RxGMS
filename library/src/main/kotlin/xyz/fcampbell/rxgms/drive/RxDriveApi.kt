package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.FileUploadPreferences
import com.google.android.gms.drive.query.Query
import rx.Observable
import rx.Single
import xyz.fcampbell.rxgms.common.RxGmsApi
import xyz.fcampbell.rxgms.drive.action.*

/**
 * Created by francois on 2016-12-22.
 */
class RxDriveApi internal constructor(
        private val context: Context
) : RxGmsApi(context, Drive.API) {
    fun fetchDriveId(resourceId: String) = rxApiClient.flatMap {
        Single.create(FetchDriveId(it, resourceId)).toObservable()
    }

    fun getAppFolder() = rxApiClient.map { Drive.DriveApi.getAppFolder(it) }

    fun getRootFolder() = rxApiClient.map { Drive.DriveApi.getRootFolder(it) }

    fun newCreateFileActivityBuilder() = Observable.just(Drive.DriveApi.newCreateFileActivityBuilder())

    fun newDriveContents() = rxApiClient.flatMap {
        Single.create(NewDriveContents(it)).toObservable()
    }

    fun newOpenFileActivityBuilder() = Observable.just(Drive.DriveApi.newOpenFileActivityBuilder())

    fun query(query: Query) = rxApiClient.flatMap {
        Single.create(QueryOnSubscribe(it, query)).toObservable()
    }

    fun requestSync() = rxApiClient.flatMap {
        Single.create(RequestSync(it)).toObservable()
    }

    fun getFileUploadPreferences() = rxApiClient.flatMap {
        Single.create(GetFileUploadPreferences(it)).toObservable()
    }

    fun setFileUploadPreferences(fileUploadPreferences: FileUploadPreferences) = rxApiClient.flatMap {
        Single.create(SetFileUploadPreferences(it, fileUploadPreferences)).toObservable()
    }
}