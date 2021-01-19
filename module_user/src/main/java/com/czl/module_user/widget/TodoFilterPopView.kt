package com.czl.module_user.widget

import android.annotation.SuppressLint
import com.czl.module_user.R
import com.czl.module_user.ui.fragment.UserTodoFragment
import com.lxj.xpopup.core.DrawerPopupView

/**
 * @author Alwyn
 * @Date 2021/1/19
 * @Description
 */
@SuppressLint("ViewConstructor")
class TodoFilterPopView(private val mFragment:UserTodoFragment) :DrawerPopupView(mFragment.requireContext()) {
    override fun getImplLayoutId(): Int {
        return R.layout.user_pop_filter
    }
}