package com.czl.lib_base.extension

import android.content.Context
import android.widget.ImageView
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.czl.lib_base.R
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File

class ImagePopLoader : XPopupImageLoader {
        override fun loadImage(
            position: Int,
            @NonNull url: Any,
            @NonNull imageView: ImageView
        ) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url).apply(
                RequestOptions().placeholder(R.drawable.default_background)
                    .override(Target.SIZE_ORIGINAL)
            ).into(imageView)
        }

        override fun getImageFile(@NonNull context: Context, @NonNull uri: Any): File? {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }