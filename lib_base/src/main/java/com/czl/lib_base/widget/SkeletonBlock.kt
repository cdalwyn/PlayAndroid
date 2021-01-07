package com.czl.lib_base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import com.czl.lib_base.R
import java.util.*

class SkeletonBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        var duration = 500L
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SkeletonBlock)
        val d = typedArray.getInteger(R.styleable.SkeletonBlock_sb_duration, 0)
        val orientation =
            typedArray.getInt(R.styleable.SkeletonBlock_sb_orientation, LinearLayout.HORIZONTAL)
        typedArray.recycle()
        val random = Random().nextBoolean()
        post {
            var v = Random().nextFloat()
            if (v < 0.3) v = 0.3f else if (v > 0.8) v = 0.8f
            val animation: Animation
            animation = if (orientation == LinearLayout.HORIZONTAL) {
                ScaleAnimation(
                    if (random) 1f else v, if (random) v else 1f,
                    1f, 1f, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, 0f
                )
            } else {
                ScaleAnimation(
                    1f, 1f, if (random) 1f else v, if (random) v else 1f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
                )
            }
            animation.setDuration(if (d == 0) duration else d.toLong())
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startAnimation(animation)
        }
    }
}