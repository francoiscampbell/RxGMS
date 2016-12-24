package xyz.fcampbell.rxgms.drive

import android.content.Context
import com.google.android.gms.drive.DriveApi
import rx.Observable
import xyz.fcampbell.rxgms.drive.onsubscribe.FetchDriveIdOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
class RxDriveApi internal constructor(
        private val ctx: Context
) {
    fun fetchDriveId(s: String): Observable<DriveApi.DriveIdResult> {
        return Observable.create(FetchDriveIdOnSubscribe(ctx, s))
    }
}