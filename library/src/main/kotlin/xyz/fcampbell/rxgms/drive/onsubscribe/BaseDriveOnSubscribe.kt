package xyz.fcampbell.rxgms.drive.onsubscribe

import android.content.Context
import com.google.android.gms.drive.Drive
import xyz.fcampbell.rxgms.common.onsubscribe.BaseOnSubscribe

/**
 * Created by francois on 2016-12-24.
 */
internal abstract class BaseDriveOnSubscribe<T>(
        private val ctx: Context
) : BaseOnSubscribe<T>(ctx, Drive.API) {
}