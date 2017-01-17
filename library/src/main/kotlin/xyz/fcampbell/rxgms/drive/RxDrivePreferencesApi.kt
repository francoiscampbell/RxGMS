package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DrivePreferencesApi
import com.google.android.gms.drive.DrivePreferencesApi.FileUploadPreferencesResult
import com.google.android.gms.drive.FileUploadPreferences
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2016-12-22.
 */
@Suppress("unused")
class RxDrivePreferencesApi(
        apiClientDescriptor: ApiClientDescriptor,
        vararg scopes: Scope
) : RxGmsApi<DrivePreferencesApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Drive.API, Drive.DrivePreferencesApi, null, *scopes)
) {
    constructor(
            context: Context,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), *scopes)

    fun getFileUploadPreferences(): Observable<FileUploadPreferencesResult> {
        return fromPendingResult { getFileUploadPreferences(it) }
    }

    fun setFileUploadPreferences(fileUploadPreferences: FileUploadPreferences): Observable<Status> {
        return fromPendingResult { setFileUploadPreferences(it, fileUploadPreferences) }
    }
}