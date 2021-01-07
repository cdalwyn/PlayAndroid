package com.czl.lib_base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FabScrollBehavior  // 因为需要在布局xml中引用，所以必须实现该构造方法
    (context: Context?, attrs: AttributeSet?) : FloatingActionButton.Behavior(context, attrs) {
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: FloatingActionButton,
        directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int
    ): Boolean {
        // 确保滚动方向为垂直方向
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: FloatingActionButton,
        target: View, dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed
        )
        if (dyConsumed > 0) { // 向下滑动
            animateOut(child)
        } else if (dyConsumed < 0) { // 向上滑动
            animateIn(child)
        }
    }

    // FAB移出屏幕动画（隐藏动画）
    private fun animateOut(fab: FloatingActionButton) {
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
//        int bottomMargin = layoutParams.bottomMargin;
//        fab.animate().translationY(fab.getHeight() + bottomMargin).setInterpolator(new LinearInterpolator()).start();
        fab.animate().scaleX(0f).scaleY(0f).setDuration(200).setInterpolator(LinearInterpolator())
            .start()
    }

    // FAB移入屏幕动画（显示动画）
    private fun animateIn(fab: FloatingActionButton) {
//        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        fab.animate().scaleX(1f).scaleY(1f).setDuration(200).setInterpolator(LinearInterpolator())
            .start()
        fab.visibility = View.VISIBLE
    }
}