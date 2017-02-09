package xyz.fcampbell.rxplayservices.wearable

import android.content.Context
import android.net.Uri
import com.google.android.gms.common.api.Status
import com.google.android.gms.wearable.CapabilityApi
import com.google.android.gms.wearable.Wearable
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Wearable.CapabilityApi]
 */
@Suppress("unused")
class RxCapabilityApi(
        apiClientDescriptor: ApiClientDescriptor,
        wearableOptions: Wearable.WearableOptions
) : RxPlayServicesApi<CapabilityApi, Wearable.WearableOptions>(
        apiClientDescriptor,
        ApiDescriptor(Wearable.API, Wearable.CapabilityApi, wearableOptions)
) {
    constructor(
            context: Context,
            wearableOptions: Wearable.WearableOptions
    ) : this(ApiClientDescriptor(context), wearableOptions)

    fun getCapability(capability: String, nodeFilter: Int): Observable<CapabilityApi.GetCapabilityResult> {
        return fromPendingResult { getCapability(it, capability, nodeFilter) }
    }

    fun getAllCapabilities(nodeFilter: Int): Observable<CapabilityApi.GetAllCapabilitiesResult> {
        return fromPendingResult { getAllCapabilities(it, nodeFilter) }
    }

    fun addLocalCapability(capability: String): Observable<CapabilityApi.AddLocalCapabilityResult> {
        return fromPendingResult { addLocalCapability(it, capability) }
    }

    fun removeLocalCapability(capability: String): Observable<CapabilityApi.RemoveLocalCapabilityResult> {
        return fromPendingResult { removeLocalCapability(it, capability) }
    }

    fun addCapabilityListener(listener: CapabilityApi.CapabilityListener, capability: String): Observable<Status> {
        return fromPendingResult { addCapabilityListener(it, listener, capability) }
    }

    fun removeCapabilityListener(listener: CapabilityApi.CapabilityListener, capability: String): Observable<Status> {
        return fromPendingResult { removeCapabilityListener(it, listener, capability) }
    }

    fun addListener(listener: CapabilityApi.CapabilityListener, uri: Uri, filterType: Int): Observable<Status> {
        return fromPendingResult { addListener(it, listener, uri, filterType) }
    }

    fun removeListener(listener: CapabilityApi.CapabilityListener): Observable<Status> {
        return fromPendingResult { removeListener(it, listener) }
    }
}