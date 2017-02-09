package xyz.fcampbell.rxplayservices.identity

import android.content.Context
import xyz.fcampbell.rxplayservices.base.ApiClientDescriptor
import xyz.fcampbell.rxplayservices.base.ApiDescriptor
import xyz.fcampbell.rxplayservices.base.RxPlayServicesApi

/**
 * Wraps [Address]
 */
class RxAddress(
        apiClientDescriptor: ApiClientDescriptor
) : RxPlayServicesApi<Unit, Address.AddressOptions>(
        apiClientDescriptor,
        ApiDescriptor(Address.API, Unit)
) {
    constructor(
            context: Context
    ) : this(ApiClientDescriptor(context))

    fun requestUserAddress(userAddressRequest: UserAddressRequest): Completable {
        return toCompletable { Address.requestUserAddress(it, userAddressRequest, 25) }//todo review
    }
}