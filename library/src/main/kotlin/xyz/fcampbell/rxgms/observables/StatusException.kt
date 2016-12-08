package xyz.fcampbell.rxgms.observables

import com.google.android.gms.common.api.Status

class StatusException(val status: Status) : Throwable()
