package xyz.fcampbell.rxgms.games

import android.content.Context
import android.net.Uri
import com.google.android.gms.common.api.Status
import com.google.android.gms.wearable.*
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxDataApi(
        apiClientDescriptor: ApiClientDescriptor,
        wearableOptions: Wearable.WearableOptions
) : RxGmsApi<DataApi, Wearable.WearableOptions>(
        apiClientDescriptor,
        ApiDescriptor(Wearable.API, Wearable.DataApi, wearableOptions)
) {
    constructor(
            context: Context,
            wearableOptions: Wearable.WearableOptions
    ) : this(ApiClientDescriptor(context), wearableOptions)

    fun putDataItem(putDataRequest: PutDataRequest): Observable<DataApi.DataItemResult> {
        return fromPendingResult { putDataItem(it, putDataRequest) }
    }

    fun getDataItem(uri: Uri): Observable<DataApi.DataItemResult> {
        return fromPendingResult { getDataItem(it, uri) }
    }

    fun getDataItems(): Observable<DataItemBuffer> {
        return fromPendingResult { getDataItems(it) }
    }

    fun getDataItems(uri: Uri): Observable<DataItemBuffer> {
        return fromPendingResult { getDataItems(it, uri) }
    }

    fun getDataItems(uri: Uri, filterType: Int): Observable<DataItemBuffer> {
        return fromPendingResult { getDataItems(it, uri, filterType) }
    }

    fun deleteDataItems(uri: Uri): Observable<DataApi.DeleteDataItemsResult> {
        return fromPendingResult { deleteDataItems(it, uri) }
    }

    fun deleteDataItems(uri: Uri, filterType: Int): Observable<DataApi.DeleteDataItemsResult> {
        return fromPendingResult { deleteDataItems(it, uri, filterType) }
    }

    fun getFdForAsset(asset: Asset): Observable<DataApi.GetFdForAssetResult> {
        return fromPendingResult { getFdForAsset(it, asset) }
    }

    fun getFdForAsset(asset: DataItemAsset): Observable<DataApi.GetFdForAssetResult> {
        return fromPendingResult { getFdForAsset(it, asset) }
    }

    fun addListener(listener: DataApi.DataListener): Observable<Status> {
        return fromPendingResult { addListener(it, listener) }
    }

    fun addListener(listener: DataApi.DataListener, uri: Uri, filterType: Int): Observable<Status> {
        return fromPendingResult { addListener(it, listener, uri, filterType) }
    }

    fun removeListener(listener: DataApi.DataListener): Observable<Status> {
        return fromPendingResult { removeListener(it, listener) }
    }
}