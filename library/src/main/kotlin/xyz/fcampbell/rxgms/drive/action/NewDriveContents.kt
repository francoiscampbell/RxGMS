package xyz.fcampbell.rxgms.drive.action

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveApi
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-24.
 */
internal class NewDriveContents(
        apiClient: GoogleApiClient
) : PendingResultOnSubscribe<DriveApi.DriveContentsResult>(
        Drive.DriveApi.newDriveContents(apiClient)
)