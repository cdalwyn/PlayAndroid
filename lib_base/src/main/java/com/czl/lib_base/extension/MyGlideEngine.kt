package com.czl.lib_base.extension;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.engine.ImageEngine;

public class MyGlideEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions requestOptions = new RequestOptions()
                .override(resize, resize)
                .placeholder(placeholder)
                .centerCrop();
        Glide.with(context)
                .load(uri)
                .thumbnail(0.8f)
//                .transition(DrawableTransitionOptions.withCrossFade())//渐变动画
                .apply(requestOptions)
                .preload();
        Glide.with(context)
                .load(uri)
                .apply(requestOptions)
                .into(imageView);

    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {

        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions()
                        .override(resize, resize)
                        .placeholder(placeholder)
                        .centerCrop())
                .into(imageView);

    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions requestOptions = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .fitCenter();
        Glide.with(context)
                .load(uri)
                .thumbnail(0.8f)
//                .transition(DrawableTransitionOptions.withCrossFade())//渐变动画
                .apply(requestOptions)
                .preload();
        Glide.with(context)
                .load(uri)
                .apply(requestOptions)
                .into(imageView);


    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {

        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(new RequestOptions()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .override(resizeX, resizeY)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);

    }

    @Override
    public boolean supportAnimatedGif() {
        return false;
    }
}