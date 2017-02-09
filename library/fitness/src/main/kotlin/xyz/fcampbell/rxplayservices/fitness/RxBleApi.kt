package xyz.fcampbell.rxplayservices.fitness

import android.content.Context
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.BleApi
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.BleDevice
import com.google.android.gms.fitness.request.BleScanCallback
import com.google.android.gms.fitness.request.StartBleScanRequest
import com.google.android.gms.fitness.result.BleDevicesResult
import io.reactivex.Observable
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Fitness.BleApi]
 */
@Suppress("unused")
class RxBleApi(
        apiClientDescriptor: ApiClientDescriptor,
        options: Api.ApiOptions.NoOptions,
        vararg scopes: Scope
) : RxPlayServicesApi<BleApi, Api.ApiOptions.NoOptions>(
        apiClientDescriptor,
        ApiDescriptor(Fitness.BLE_API, Fitness.BleApi, options, *scopes)
) {
    constructor(
            context: Context,
            options: Api.ApiOptions.NoOptions,
            vararg scopes: Scope
    ) : this(ApiClientDescriptor(context), options, *scopes)

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    fun startBleScan(request: StartBleScanRequest): Observable<Status> {
        return fromPendingResult { startBleScan(it, request) }
    }

    fun stopBleScan(callback: BleScanCallback): Observable<Status> {
        return fromPendingResult { stopBleScan(it, callback) }
    }

    fun claimBleDevice(bleDevice: BleDevice): Observable<Status> {
        return fromPendingResult { claimBleDevice(it, bleDevice) }
    }

    fun claimBleDevice(deviceAddress: String): Observable<Status> {
        return fromPendingResult { claimBleDevice(it, deviceAddress) }
    }

    fun unclaimBleDevice(deviceAddress: String): Observable<Status> {
        return fromPendingResult { unclaimBleDevice(it, deviceAddress) }
    }

    fun unclaimBleDevice(bleDevice: BleDevice): Observable<Status> {
        return fromPendingResult { unclaimBleDevice(it, bleDevice) }
    }

    fun listClaimedBleDevices(): Observable<BleDevicesResult> {
        return fromPendingResult { listClaimedBleDevices(it) }
    }
}