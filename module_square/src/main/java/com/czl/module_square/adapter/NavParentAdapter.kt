package com.czl.module_square.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.data.bean.NavigationBean
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareItemNavParentBinding
import com.czl.module_square.ui.fragment.NavigateFragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * @author Alwyn
 * @Date 2020/12/4
 * @Description
 */
class NavParentAdapter(val mFragment: NavigateFragment) :
    BaseQuickAdapter<NavigationBean, BaseDataBindingHolder<SquareItemNavParentBinding>>(
        R.layout.square_item_nav_parent
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<SquareItemNavParentBinding>,
        item: NavigationBean
    ) {
        holder.dataBinding?.apply {
            data = item
            val childAdapter = NavChildAdapter(mFragment)
            ryCommon.apply {
                layoutManager = FlexboxLayoutManager(context,FlexDirection.ROW,FlexWrap.WRAP).apply { justifyContent = JustifyContent.FLEX_START }
                adapter = childAdapter
            }
            childAdapter.setList(item.articles)
            executePendingBindings()
        }
    }
}