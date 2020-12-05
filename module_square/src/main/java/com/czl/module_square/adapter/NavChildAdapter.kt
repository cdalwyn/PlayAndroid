package com.czl.module_square.adapter

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.NavigationBean
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareItemNavChildBinding
import com.czl.module_square.ui.fragment.NavigateFragment

/**
 * @author Alwyn
 * @Date 2020/12/4
 * @Description
 */
class NavChildAdapter(val mFragment: NavigateFragment) :BaseQuickAdapter<NavigationBean.Article,BaseDataBindingHolder<SquareItemNavChildBinding>>(
    R.layout.square_item_nav_child) {
    override fun convert(
        holder: BaseDataBindingHolder<SquareItemNavChildBinding>,
        item: NavigationBean.Article
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@NavChildAdapter
            executePendingBindings()
        }
    }
    val onItemClickCommand:BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is NavigationBean.Article){
            mFragment.viewModel.startFragment(AppConstants.Router.Base.F_WEB, Bundle().apply {
                putString(AppConstants.BundleKey.WEB_URL,it.link)
            })
        }
    })
}