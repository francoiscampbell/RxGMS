package xyz.fcampbell.rxgms.identity

import android.content.Context
import com.google.android.gms.identity.intents.Address
import com.google.android.gms.identity.intents.UserAddressRequest
import io.reactivex.Completable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Wraps [Address]
 */
class RxAddress(
        apiClientDescriptor: ApiClientDescriptor
) : RxGmsApi<Unit, Address.AddressOptions>(
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