package com.czl.lib_base.binding.viewadapter.webview

import android.text.TextUtils
import android.webkit.WebView
import androidx.databinding.BindingAdapter

object ViewAdapter {
    @JvmStatic
    @BindingAdapter("render")
    fun loadHtml(webView: WebView, html: String?) {
        if (!TextUtils.isEmpty(html)) {
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        }
    }
}