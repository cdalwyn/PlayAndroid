package com.czl.module_web.widget

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.czl.lib_base.base.BaseActivity
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
@SuppressLint("ViewConstructor")
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
                40f,
                40f,
                0f,
                0f
            )
            executePendingBindings()
        }
    }

    val onBackHomeClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.agentWeb?.urlLoader?.loadUrl(mFragment.homeUrl)
        dismiss()
    })
    val onShareSquareClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        dismiss()
        mFragment.viewModel.uC.showSharePopEvent.postValue(SparseArray<String>(2).apply {
            put(0, mFragment.currentTitle)
            put(1, mFragment.currentLink)
        })
    })
    val onRefreshClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.agentWeb?.urlLoader?.reload()
        dismiss()
    })
    val onSettingClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.viewModel.startContainerActivity(AppConstants.Router.User.F_USER_SETTING)
        dismiss()
    })
    val onOpenCollectClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.viewModel.startContainerActivity(
            AppConstants.Router.User.F_USER_COLLECT,
            Bundle().apply {
                putString(AppConstants.BundleKey.WEB_MENU_KEY, "collect")
            })
        dismiss()
    })
    val onHistoryClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.viewModel.startContainerActivity(AppConstants.Router.User.F_USER_BROWSE)
        dismiss()
    })
    val onShareOtherClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mFragment.currentLink)
        intent.putExtra(Intent.EXTRA_TITLE, mFragment.currentTitle)
        mFragment.startActivity(Intent.createChooser(intent, "分享到"))
        dismiss()
    })
    val onExitClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        mFragment.requireActivity().finish()
    })

    override fun onDestroy() {
        dataBinding?.unbind()
        super.onDestroy()
    }
}