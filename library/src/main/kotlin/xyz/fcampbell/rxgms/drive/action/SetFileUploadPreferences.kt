package xyz.fcampbell.rxgms.drive.action

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.FileUploadPreferences
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-24.
 */
internal class SetFileUploadPreferences(
        apiClient: GoogleApiClient,
        fileUploadPreferences: FileUploadPreferences
) : PendingResultOnSubscribe<Status>(
        Drive.DrivePreferencesApi.setFileUploadPreferences(apiClient, fileUploadPreferences)
)