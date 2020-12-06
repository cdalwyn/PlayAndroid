package com.czl.module_main.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.HomeArticleBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.module_main.R
import com.czl.module_main.databinding.MainItemHomeBinding
import com.czl.module_main.ui.fragment.HomeFragment

/**
 * @author Alwyn
 * @Date 2020/11/27
 * @Description
 */
class HomeArticleAdapter(val mFragment: HomeFragment) :
    BaseQuickAdapter<HomeArticleBean.Data, BaseDataBindingHolder<MainItemHomeBinding>>(
        R.layout.main_item_home
    ) {
    val tvShare = " 分享 "
    val tvAuthor = " 作者 "

    override fun convert(
        holder: BaseDataBindingHolder<MainItemHomeBinding>,
        item: HomeArticleBean.Data
    ) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@HomeArticleAdapter
            executePendingBindings()
        }
    }

//    override fun convert(
//        holder: BaseDataBindingHolder<MainItemHomeBinding>,
//        item: HomeArticleBean.Data,
//        payloads: List<Any>
//    ) {
//        super.convert(holder, item, payloads)
//        if (payloads.isEmpty()) {
//            return convert(holder, item)
//        }
//        if (payloads[0] == 0) {
//            holder.dataBinding?.ivCollect?.setImageResource(R.drawable.ic_like_off_gray)
//        } else {
//            holder.dataBinding?.ivCollect?.setImageResource(R.drawable.ic_like_on)
//        }
//    }

    val onArticleItemClick: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is HomeArticleBean.Data) {
            val bundle = Bundle()
            bundle.putString(AppConstants.BundleKey.WEB_URL, it.link)
            mFragment.viewModel.startContainerActivity(AppConstants.Router.Web.F_WEB, bundle)
        }
    })

    val diffConfig = object : DiffUtil.ItemCallback<HomeArticleBean.Data>() {
        override fun areItemsTheSame(
            oldItem: HomeArticleBean.Data,
            newItem: HomeArticleBean.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: HomeArticleBean.Data,
            newItem: HomeArticleBean.Data
        ): Boolean {
            return oldItem.title == newItem.title && oldItem.collect == newItem.collect
        }
    }

    val onCollectClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is HomeArticleBean.Data) {
            if (!it.collect) {
                mFragment.viewModel.collectArticle(it.id)
                    .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                        override fun onResult(t: BaseBean<*>) {
                            if (t.errorCode == 0) {
                                LiveBusCenter.postRefreshUserFmEvent()
                                mFragment.showSuccessToast("收藏成功")
                                it.collect = true
                                // 同步热门项目列表的相同item
                                val list = mFragment.mProjectAdapter.data.filter { x->x.id==it.id }
                                if (list.isNotEmpty()) list[0].collect = true
                            } else {
                                mFragment.showErrorToast(t.errorMsg)
                            }
                        }

                        override fun onFailed(msg: String?) {
                            mFragment.showErrorToast(msg)
                        }
                    })
            } else {
                mFragment.viewModel.unCollectArticle(it.id)
                    .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                        override fun onResult(t: BaseBean<*>) {
                            if (t.errorCode == 0) {
                                LiveBusCenter.postRefreshUserFmEvent()
                                it.collect = false
                                // 同步热门项目列表的相同item
                                val list = mFragment.mProjectAdapter.data.filter { x->x.id==it.id }
                                if (list.isNotEmpty()) list[0].collect = false
                            } else {
                                mFragment.showErrorToast(t.errorMsg)
                            }
                        }

                        override fun onFailed(msg: String?) {
                            mFragment.showErrorToast(msg)
                        }
                    })
            }
        }
    })


}