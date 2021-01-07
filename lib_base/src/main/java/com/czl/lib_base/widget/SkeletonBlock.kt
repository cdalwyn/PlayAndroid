package com.czl.lib_base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.czl.lib_base.R;

import java.util.Random;

public class SkeletonBlock extends View {
    public static int duration = 500;

    public SkeletonBlock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkeletonBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SkeletonBlock);
        final int d = typedArray.getInteger(R.styleable.SkeletonBlock_sb_duration, 0);
        final int orientation = typedArray.getInt(R.styleable.SkeletonBlock_sb_orientation, LinearLayout.HORIZONTAL);
        typedArray.recycle();
        final boolean random = new Random().nextBoolean();
        post(() -> {

            float v = new Random().nextFloat();
            if (v < 0.3) v = 0.3f;
            else if (v > 0.8) v = 0.8f;

            Animation animation;
            if (orientation == LinearLayout.HORIZONTAL) {
                animation = new ScaleAnimation(random ? 1 : v, random ? v : 1, 1, 1,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            } else {
                animation = new ScaleAnimation(1, 1, random ? 1 : v, random ? v : 1,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            }
            animation.setDuration(d == 0 ? duration : d);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setRepeatCount(Animation.INFINITE);


            startAnimation(animation);
        });

    }

}