package com.czl.lib_base.extension

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.zhihu.matisse.engine.ImageEngine

class MyGlideEngine : ImageEngine {
    override fun loadThumbnail(
        context: Context,
        resize: Int,
        placeholder: Drawable,
        imageView: ImageView,
        uri: Uri
    ) {
        val requestOptions = RequestOptions()
            .override(resize, resize)
            .placeholder(placeholder)
            .centerCrop()
        Glide.with(context)
            .load(uri)
            .thumbnail(0.8f) //                .transition(DrawableTransitionOptions.withCrossFade())//渐变动画
            .apply(requestOptions)
            .preload()
        Glide.with(context)
            .load(uri)
            .apply(requestOptions)
            .into(imageView)
    }

    override fun loadGifThumbnail(
        context: Context,
        resize: Int,
        placeholder: Drawable,
        imageView: ImageView,
        uri: Uri
    ) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .apply(
                RequestOptions()
                    .override(resize, resize)
                    .placeholder(placeholder)
                    .centerCrop()
            )
            .into(imageView)
    }

    override fun loadImage(
        context: Context,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView,
        uri: Uri
    ) {
        val requestOptions = RequestOptions()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .override(resizeX, resizeY)
            .priority(Priority.HIGH)
            .fitCenter()
        Glide.with(context)
            .load(uri)
            .thumbnail(0.8f) //                .transition(DrawableTransitionOptions.withCrossFade())//渐变动画
            .apply(requestOptions)
            .preload()
        Glide.with(context)
            .load(uri)
            .apply(requestOptions)
            .into(imageView)
    }

    override fun loadGifImage(
        context: Context,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView,
        uri: Uri
    ) {
        Glide.with(context)
            .asGif()
            .load(uri)
            .apply(
                RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(resizeX, resizeY)
                    .priority(Priority.HIGH)
                    .fitCenter()
            )
            .into(imageView)
    }

    override fun supportAnimatedGif(): Boolean {
        return false
    }
}