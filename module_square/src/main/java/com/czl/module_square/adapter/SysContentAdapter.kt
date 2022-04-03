package com.czl.module_square.adapter

import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.binding.command.BindingConsumer
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.SystemDetailBean
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.lib_base.extension.ImagePopLoader
import com.czl.module_square.R
import com.czl.module_square.databinding.SquareItemArticleBinding
import com.czl.module_square.databinding.SquareItemArticleImageBinding
import com.czl.module_square.ui.fragment.SysContentFragment
import com.czl.module_square.ui.fragment.SystemDetailFragment
import com.lxj.xpopup.XPopup

/**
 * @author Alwyn
 * @Date 2020/12/5
 * @Description
 */
class SysContentAdapter(val mFragment: SysContentFragment) :
    BaseDelegateMultiAdapter<SystemDetailBean.Data, BaseViewHolder>() {

    val tvShare = " 分享 "
    val tvAuthor = " 作者 "

//    override fun convert(
//        holder: BaseDataBindingHolder<SquareItemArticleBinding>,
//        item: SystemDetailBean.Data
//    ) {
//        holder.dataBinding?.apply {
//            data = item
//            adapter = this@SysContentAdapter
//            executePendingBindings()
//        }
//    }

    init {
        setMultiTypeDelegate(object :BaseMultiTypeDelegate<SystemDetailBean.Data>(){
            override fun getItemType(data: List<SystemDetailBean.Data>, position: Int): Int {
                if (data[position].envelopePic.isEmpty()){
                    return AppConstants.Constants.PLAIN_TEXT_TYPE
                }else{
                    return AppConstants.Constants.IMAGE_TEXT_TYPE
                }
            }
        })
        getMultiTypeDelegate()?.apply {
            addItemType(AppConstants.Constants.PLAIN_TEXT_TYPE,R.layout.square_item_article)
            addItemType(AppConstants.Constants.IMAGE_TEXT_TYPE,R.layout.square_item_article_image)
        }
    }

    override fun convert(holder: BaseViewHolder, item: SystemDetailBean.Data) {

        when(holder.itemViewType){
            AppConstants.Constants.PLAIN_TEXT_TYPE->{
                val dataBinding = DataBindingUtil.bind<SquareItemArticleBinding>(holder.itemView)
                dataBinding?.apply {
                    data = item
                    adapter = this@SysContentAdapter
                    executePendingBindings()
                }
            }
            AppConstants.Constants.IMAGE_TEXT_TYPE->{
                val dataBinding = DataBindingUtil.bind<SquareItemArticleImageBinding>(holder.itemView)
                dataBinding?.apply {
                    data = item
                    adapter = this@SysContentAdapter
                    executePendingBindings()
                }
            }
        }

    }

    val onItemPicClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is SystemDetailBean.Data) {
            XPopup.Builder(context)
                .asImageViewer(
                    getViewByPosition(
                        getItemPosition(it),
                        R.id.iv_cover
                    ) as ImageView, it.envelopePic, ImagePopLoader()
                )
                .show()
        }
    })

    val onArticleItemClick: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is SystemDetailBean.Data) {
            val bundle = Bundle()
            bundle.putString(AppConstants.BundleKey.WEB_URL, it.link)
            mFragment.startContainerActivity(AppConstants.Router.Web.F_WEB, bundle)
        }
    })

    val onNameClick: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is SystemDetailBean.Data) {
            mFragment.startContainerActivity(AppConstants.Router.User.F_USER_DETAIL,Bundle().apply {
                if (it.userId == -1){
                    putString(AppConstants.BundleKey.USER_NAME,it.author)
                }
                putString(AppConstants.BundleKey.USER_ID,it.userId.toString())
            })
        }
    })

    val onCollectClickCommand: BindingCommand<Any> = BindingCommand(BindingConsumer {
        if (it is SystemDetailBean.Data) {
            if (!it.collect) {
                mFragment.viewModel.collectArticle(it.id)
                    .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                        override fun onResult(t: BaseBean<*>) {
                            if (t.errorCode == 0) {
                                LiveBusCenter.postRefreshUserFmEvent()
                                mFragment.showSuccessToast("收藏成功")
                                it.collect = true

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

    val diffConfig = object : DiffUtil.ItemCallback<SystemDetailBean.Data>() {
        override fun areItemsTheSame(
            oldItem: SystemDetailBean.Data,
            newItem: SystemDetailBean.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SystemDetailBean.Data,
            newItem: SystemDetailBean.Data
        ): Boolean {
            return oldItem.title == newItem.title
        }

    }


}