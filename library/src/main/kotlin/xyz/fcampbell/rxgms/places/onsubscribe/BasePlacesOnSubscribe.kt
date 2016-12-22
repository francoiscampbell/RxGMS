package xyz.fcampbell.rxgms.places.onsubscribe

import android.content.Context
import com.google.android.gms.location.places.Places
import xyz.fcampbell.rxgms.common.onsubscribe.BaseOnSubscribe

/**
 * Created by francois on 2016-12-22.
 */
internal abstract class BasePlacesOnSubscribe<T>(
        ctx: Context
) : BaseOnSubscribe<T>(ctx, Places.GEO_DATA_API, Places.PLACE_DETECTION_API)