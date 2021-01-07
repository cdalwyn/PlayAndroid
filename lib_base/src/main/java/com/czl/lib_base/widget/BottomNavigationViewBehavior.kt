package com.czl.lib_base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout

/**
 * 与toolbar联动隐藏底部菜单
 *
 * @author LeonDevLifeLog <leondevlifelog></leondevlifelog>@gmail.com>
 * @date 2018-02-24 08:59
 * @since V4.0.0
 */
class BottomNavigationViewBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        (child.layoutParams as CoordinatorLayout.LayoutParams).topMargin = parent
            .measuredHeight - child.measuredHeight
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        //得到依赖View的滑动距离
        val top = ((dependency
            .layoutParams as CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior?)!!.topAndBottomOffset
        //因为BottomNavigation的滑动与ToolBar是反向的，所以取负值
        ViewCompat.setTranslationY(
            child, -(top * child.measuredHeight / dependency
                .measuredHeight).toFloat()
        )
        return false
    }
}