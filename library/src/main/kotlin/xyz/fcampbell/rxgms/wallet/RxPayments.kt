package xyz.fcampbell.rxgms.games

import android.content.Context
import com.google.android.gms.common.api.BooleanResult
import com.google.android.gms.wallet.*
import io.reactivex.Completable
import io.reactivex.Observable
import xyz.fcampbell.rxgms.common.ApiClientDescriptor
import xyz.fcampbell.rxgms.common.ApiDescriptor
import xyz.fcampbell.rxgms.common.RxGmsApi

/**
 * Created by francois on 2017-01-13.
 */
@Suppress("unused")
class RxPayments(
        apiClientDescriptor: ApiClientDescriptor,
        walletOptions: Wallet.WalletOptions
) : RxGmsApi<Payments, Wallet.WalletOptions>(
        apiClientDescriptor,
        ApiDescriptor(Wallet.API, Wallet.Payments, walletOptions)
) {
    constructor(
            context: Context,
            walletOptions: Wallet.WalletOptions
    ) : this(ApiClientDescriptor(context), walletOptions)


    fun loadMaskedWallet(request: MaskedWalletRequest, requestCode: Int): Completable {
        return toCompletable { loadMaskedWallet(it, request, requestCode) }
    }

    fun loadFullWallet(request: FullWalletRequest, requestCode: Int): Completable {
        return toCompletable { loadFullWallet(it, request, requestCode) }
    }

    fun changeMaskedWallet(googleTransactionId: String, merchantTransactionId: String, requestCode: Int): Completable {
        return toCompletable { changeMaskedWallet(it, googleTransactionId, merchantTransactionId, requestCode) }
    }

    fun isReadyToPay(request: IsReadyToPayRequest): Observable<BooleanResult> {
        return fromPendingResult { isReadyToPay(it, request) }
    }
}