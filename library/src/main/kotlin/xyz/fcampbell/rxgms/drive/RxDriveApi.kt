package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.*
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

    fun getAppFolder(): Observable<DriveFolder> {
        return rxApiClient.map { Drive.DriveApi.getAppFolder(it) }
    }

    fun getRootFolder(): Observable<DriveFolder> {
        return rxApiClient.map { Drive.DriveApi.getRootFolder(it) }
    }

    fun newCreateFileActivityBuilder(): Observable<CreateFileActivityBuilder> {
        return Observable.just(Drive.DriveApi.newCreateFileActivityBuilder())
    }

    fun newDriveContents(): Observable<DriveApi.DriveContentsResult> {
        return rxApiClient.flatMap {
            Single.create(NewDriveContents(it)).toObservable()
        }
    }

    fun newOpenFileActivityBuilder(): Observable<OpenFileActivityBuilder> {
        return Observable.just(Drive.DriveApi.newOpenFileActivityBuilder())
    }

    fun query(query: Query): Observable<DriveApi.MetadataBufferResult> {
        return rxApiClient.flatMap {
            Single.create(QueryOnSubscribe(it, query)).toObservable()
        }
    }

    fun requestSync(): Observable<Status> {
        return rxApiClient.flatMap {
            Single.create(RequestSync(it)).toObservable()
        }
    }

    fun getFileUploadPreferences(): Observable<DrivePreferencesApi.FileUploadPreferencesResult> {
        return rxApiClient.flatMap {
            Single.create(GetFileUploadPreferences(it)).toObservable()
        }
    }

    fun setFileUploadPreferences(fileUploadPreferences: FileUploadPreferences): Observable<Status> {
        return rxApiClient.flatMap {
            Single.create(SetFileUploadPreferences(it, fileUploadPreferences)).toObservable()
        }
    }
}