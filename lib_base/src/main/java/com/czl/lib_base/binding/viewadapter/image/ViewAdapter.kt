package com.czl.lib_base.binding.viewadapter.image

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableInt
import com.czl.lib_base.extension.loadUrl

object ViewAdapter {
    @JvmStatic
    @BindingAdapter(value = ["url", "placeholderRes","errorRes"], requireAll = false)
    fun setImageUri(imageView: ImageView, url: String?, placeholderRes: Int,errorRes:Int) {
        if (!TextUtils.isEmpty(url)) {
            imageView.loadUrl(url,placeholderRes,errorRes)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["src"], requireAll = false)
    fun setImageRes(imageView: ImageView, resId: ObservableInt?) {
        if (resId == null) {
            return
        }
        if (resId.get() != 0) {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(resId.get())
        } else {
            imageView.visibility = View.INVISIBLE
        }
    }
}