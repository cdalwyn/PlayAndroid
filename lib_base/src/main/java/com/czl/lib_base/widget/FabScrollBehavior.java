package com.czl.lib_base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class FabScrollBehavior extends FloatingActionButton.Behavior {

    // 因为需要在布局xml中引用，所以必须实现该构造方法
    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NotNull final CoordinatorLayout coordinatorLayout, @NotNull final FloatingActionButton child,
                                       @NotNull final View directTargetChild, @NotNull final View target, final int nestedScrollAxes, final int type) {
        // 确保滚动方向为垂直方向
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NotNull final CoordinatorLayout coordinatorLayout, @NotNull final FloatingActionButton child,
                               @NotNull final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0) { // 向下滑动
            animateOut(child);
        } else if (dyConsumed < 0) { // 向上滑动
            animateIn(child);
        }
    }

    // FAB移出屏幕动画（隐藏动画）
    private void animateOut(FloatingActionButton fab) {
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
//        int bottomMargin = layoutParams.bottomMargin;
//        fab.animate().translationY(fab.getHeight() + bottomMargin).setInterpolator(new LinearInterpolator()).start();
        fab.animate().scaleX(0).scaleY(0).setDuration(200).setInterpolator(new LinearInterpolator()).start();
    }

    // FAB移入屏幕动画（显示动画）
    private void animateIn(FloatingActionButton fab) {
//        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        fab.animate().scaleX(1f).scaleY(1f).setDuration(200).setInterpolator(new LinearInterpolator()).start();
    }

}