package com.czl.module_web.widget

import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.config.AppConstants
import com.czl.module_web.R
import com.czl.module_web.databinding.WebPopMenuBinding
import com.czl.module_web.ui.fragment.WebFragment
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils

/**
 * @author Alwyn
 * @Date 2020/12/6
 * @Description
 */
class WebMenuPop(private val mFragment: WebFragment) : BottomPopupView(mFragment.requireContext()) {
    private var dataBinding: WebPopMenuBinding? = null
    override fun getImplLayoutId(): Int {
        return R.layout.web_pop_menu
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.apply {
            pop = this@WebMenuPop
            clRoot.background = XPopupUtils.createDrawable(
                ContextCompat.getColor(context, com.czl.lib_base.R.color.white),
                30f,
                30f,
                0f,
                0f
            )
            executePendingBindings()
        }
    }

    val onBackHomeClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.agentWeb.urlLoader.loadUrl(mFragment.homeUrl)
        dismiss()
    })
    val onShareSquareClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {

    })
    val onRefreshClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.agentWeb.urlLoader.reload()
        dismiss()
    })
    val onSettingClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {

    })
    val onOpenCollectClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.viewModel.startFragment(AppConstants.Router.User.F_USER_COLLECT, Bundle().apply {
            putString(AppConstants.BundleKey.WEB_MENU_KEY,"collect")
        })
        dismiss()
    })
    val onHistoryClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {

    })
    val onShareOtherClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {

    })
    val onExitClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.requireActivity().finish()
    })

    override fun onDestroy() {
        dataBinding?.unbind()
        super.onDestroy()
    }
}