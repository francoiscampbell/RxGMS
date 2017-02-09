package xyz.fcampbell.rxplayservices.drive

import android.content.Context
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DrivePreferencesApi
import com.google.android.gms.drive.FileUploadPreferences
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Drive.DrivePreferencesApi]
 */
@Suppress("unused")
class RxDrivePreferencesApi(
        apiClientDescriptor: ApiClientDescriptor,
        vararg scopes: Scope
) : RxPlayServicesApi<DrivePreferencesApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Drive.API, Drive.DrivePreferencesApi, null, *scopes)
) {
    constructor(
            context: Context,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), *scopes)

    fun getFileUploadPreferences(): Observable<DrivePreferencesApi.FileUploadPreferencesResult> {
        return fromPendingResult { getFileUploadPreferences(it) }
    }

    fun setFileUploadPreferences(fileUploadPreferences: FileUploadPreferences): Observable<Status> {
        return fromPendingResult { setFileUploadPreferences(it, fileUploadPreferences) }
    }
}