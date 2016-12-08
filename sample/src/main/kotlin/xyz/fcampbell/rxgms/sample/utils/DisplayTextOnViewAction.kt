package xyz.fcampbell.rxgms.sample.utils

import android.widget.TextView

import rx.functions.Action1

class DisplayTextOnViewAction(private val target: TextView) : Action1<String> {

    override fun call(s: String) {
        target.text = s
    }
}
