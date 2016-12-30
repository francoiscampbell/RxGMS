package xyz.fcampbell.rxgms.drive.action

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.query.Query
import xyz.fcampbell.rxgms.common.action.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-24.
 */
internal class QueryOnSubscribe(
        apiClient: GoogleApiClient,
        query: Query
) : PendingResultOnSubscribe<DriveApi.MetadataBufferResult>(
        Drive.DriveApi.query(apiClient, query)
)