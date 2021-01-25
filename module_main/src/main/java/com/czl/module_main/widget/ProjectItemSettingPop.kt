package com.czl.module_main.widget

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.data.bean.ProjectBean
import com.czl.lib_base.data.net.RetrofitClient
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.extension.ApiSubscriberHelper
import com.czl.module_main.R
import com.czl.module_main.databinding.MainPopSettingAttachBinding
import com.czl.module_main.ui.fragment.HomeFragment
import com.lxj.xpopup.core.HorizontalAttachPopupView

/**
 * @author Alwyn
 * @Date 2020/10/31
 * @Description
 */
@SuppressLint("ViewConstructor")
class ProjectItemSettingPop(val mFragment: HomeFragment, val bean: ProjectBean.Data) :
    HorizontalAttachPopupView(mFragment.requireContext()) {

    private var dataBinding: MainPopSettingAttachBinding? = null
    val tvCollect = "收藏"
    val tvUnCollect = "取消收藏"

    override fun getImplLayoutId(): Int {
        return R.layout.main_pop_setting_attach
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.apply {
            this.data = bean
            pop = this@ProjectItemSettingPop
            executePendingBindings()
        }
    }

    override fun onDestroy() {
        dataBinding?.unbind()
        super.onDestroy()
    }

    val onCollectCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        if (!bean.collect) {
            mFragment.viewModel.collectArticle(bean.id)
                .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                    override fun onResult(t: BaseBean<*>) {
                        if (t.errorCode == 0) {
                            LiveBusCenter.postRefreshUserFmEvent()
                            mFragment.showSuccessToast("收藏成功")
                            bean.collect = true
                            // 同步热门文章列表的Item
                            val list =
                                mFragment.mArticleAdapter.data.filter { x -> x.id == bean.id }
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
            mFragment.viewModel.unCollectArticle(bean.id)
                .subscribe(object : ApiSubscriberHelper<BaseBean<*>>() {
                    override fun onResult(t: BaseBean<*>) {
                        if (t.errorCode == 0) {
                            LiveBusCenter.postRefreshUserFmEvent()
                            bean.collect = false
                            // 同步热门文章列表的Item
                            val list =
                                mFragment.mArticleAdapter.data.filter { x -> x.id == bean.id }
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
    })

    val onFindSameClick: BindingCommand<Void> = BindingCommand(BindingAction {
//        val bundle = Bundle()
//        bundle.putString(
//            AppConstants.BundleKey.WEB_URL,
//            RetrofitClient.baseUrl + bean.tags[0].url
//        )
        mFragment.startContainerActivity(AppConstants.Router.Square.F_SYS_CONTENT, Bundle().apply {
            putString(AppConstants.BundleKey.SYS_CONTENT_TITLE,bean.chapterName)
            putString("cid",bean.chapterId.toString())
        })
        dismiss()
    })

}