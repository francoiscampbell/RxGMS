package xyz.fcampbell.rxgms.identity

import android.content.Context
import com.google.android.gms.identity.intents.Address
import com.google.android.gms.identity.intents.UserAddressRequest
import rx.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
class RxAddress(
        apiClientDescriptor: ApiClientDescriptor
) : RxGmsApi<Address.AddressOptions>(
        apiClientDescriptor,
        ApiDescriptor(Address.API)
) {
    constructor(
            context: Context
    ) : this(ApiClientDescriptor(context))

    fun requestUserAddress(userAddressRequest: UserAddressRequest): Observable<Unit> {
        return apiClient.map {
            Address.requestUserAddress(it.first, userAddressRequest, 25)
        }
    }
}