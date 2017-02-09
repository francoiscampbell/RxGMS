package xyz.fcampbell.rxplayservices.drive

import android.content.Context
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

    fun getFileUploadPreferences(): Observable<FileUploadPreferencesResult> {
        return fromPendingResult { getFileUploadPreferences(it) }
    }

    fun setFileUploadPreferences(fileUploadPreferences: FileUploadPreferences): Observable<Status> {
        return fromPendingResult { setFileUploadPreferences(it, fileUploadPreferences) }
    }
}