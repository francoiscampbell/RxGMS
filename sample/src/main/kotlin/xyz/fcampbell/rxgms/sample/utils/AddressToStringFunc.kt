package xyz.fcampbell.rxgms.sample.utils

import android.location.Address

import rx.functions.Func1

object AddressToStringFunc : Func1<Address?, String> {
    override fun call(address: Address?): String {
        if (address == null) return ""

        var addressLines = ""
        for (i in 0..address.maxAddressLineIndex) {
            addressLines += address.getAddressLine(i) + '\n'
        }
        return addressLines
    }
}
