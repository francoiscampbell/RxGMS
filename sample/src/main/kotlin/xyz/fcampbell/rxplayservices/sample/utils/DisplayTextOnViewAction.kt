package xyz.fcampbell.rxplayservices.sample.utils

import android.widget.TextView
import io.reactivex.functions.Consumer

class DisplayTextOnViewAction(private val target: TextView) : Consumer<String> {

    override fun accept(s: String) {
        target.text = s
    }
}
