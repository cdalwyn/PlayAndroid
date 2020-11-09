package com.czl.lib_base.binding.viewadapter.image;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableInt;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholderRes))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"src"}, requireAll = false)
    public static void setImageRes(ImageView imageView, ObservableInt resId) {
        if (resId == null) {
            return;
        }
        if (resId.get() != 0) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(resId.get());
        }else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}

