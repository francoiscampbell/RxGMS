package xyz.fcampbell.rxgms.sample.utils

import android.location.Address
import io.reactivex.functions.Function

object AddressToStringFunc : Function<Address, String> {
    override fun apply(address: Address): String {
        var addressLines = ""
        for (i in 0..address.maxAddressLineIndex) {
            addressLines += address.getAddressLine(i) + '\n'
        }
        return addressLines
    }
}
