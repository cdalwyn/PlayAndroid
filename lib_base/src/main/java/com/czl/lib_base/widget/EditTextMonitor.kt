package com.czl.lib_base.widget

import android.text.Editable
import android.text.TextWatcher
import io.reactivex.subjects.PublishSubject

class EditTextMonitor(private val mPublishSubject: PublishSubject<String>) : TextWatcher {
    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {}

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {}

    override fun afterTextChanged(s: Editable) {
        mPublishSubject.onNext(s.toString())
    }
}