package com.czl.module_web.widget

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.ClipboardUtils
import com.czl.lib_base.binding.command.BindingAction
import com.czl.lib_base.binding.command.BindingCommand
import com.czl.lib_base.util.ToastHelper
import com.czl.module_web.R
import com.czl.module_web.databinding.WebPopLinkBinding
import com.czl.module_web.ui.fragment.WebFragment
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.core.HorizontalAttachPopupView
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.impl.PartShadowPopupView

/**
 * @author Alwyn
 * @Date 2020/12/11
 * @Description
 */
@SuppressLint("ViewConstructor")
class WebLinkPop(private val mFragment: WebFragment) :
    PartShadowPopupView(mFragment.requireContext()) {
    private var dataBinding: WebPopLinkBinding? = null
    override fun getImplLayoutId(): Int {
        return R.layout.web_pop_link
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.apply {
            executePendingBindings()
        }
    }

    val copyLinkClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        ClipboardUtils.copyText(mFragment.currentLink)
        mFragment.showSuccessToast("复制成功")
    })

    val openOnBrowserClick: BindingCommand<Void> = BindingCommand(BindingAction {
        val uri = Uri.parse(mFragment.currentLink)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        mFragment.startActivity(intent)
    })
}