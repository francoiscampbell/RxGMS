package xyz.fcampbell.rxgms.drive.onsubscribe

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.query.Query
import rx.Observable
import rx.Observer
import xyz.fcampbell.rxgms.common.onsubscribe.PendingResultOnSubscribe

/**
 * Created by francois on 2016-12-24.
 */
internal class QueryOnSubscribe(
        ctx: Context,
        private val query: Query
) : BaseDriveOnSubscribe<DriveApi.MetadataBufferResult>(ctx) {
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient, observer: Observer<in DriveApi.MetadataBufferResult>) {
        val pendingResult = Drive.DriveApi.query(apiClient, query)
        Observable.create(PendingResultOnSubscribe(pendingResult)).subscribe(observer)
    }
}