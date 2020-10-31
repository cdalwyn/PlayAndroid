package com.czl.lib_base.mvvm.ui

import android.view.ViewGroup
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.BR
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.databinding.FragmentWebBinding
import com.czl.lib_base.mvvm.viewmodel.WebFmViewModel
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import me.goldze.mvvmhabit.binding.command.BindingAction
import me.goldze.mvvmhabit.binding.command.BindingCommand

@Route(path = AppConstants.Router.Base.F_WEB)
class WebFragment : BaseFragment<FragmentWebBinding, WebFmViewModel>() {

    private lateinit var agentWeb: AgentWeb


    override fun initContentView(): Int {
        return R.layout.fragment_web
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        val url = arguments?.getString(AppConstants.BundleKey.WEB_URL)
        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.llWeb, ViewGroup.LayoutParams(-1, -1))
            .useDefaultIndicator(ContextCompat.getColor(requireContext(), R.color.md_theme_red), 1)
            .interceptUnkownUrl()
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    viewModel.tvTitle.set(title)
                }
            })
            .createAgentWeb()
            .ready()
            .go(url)
        viewModel.ivToolbarIconRes = R.drawable.ic_delete_32dp
        viewModel.ivToolbarIconOnClick = BindingCommand(BindingAction {
            requireActivity().finish()
        })
    }

    override fun back() {
        if (agentWeb.webCreator.webView.canGoBack()) {
            agentWeb.back()
            return
        }
        super.back()
    }

    override fun onBackPressedSupport(): Boolean {
        if (agentWeb.webCreator.webView.canGoBack()) {
            agentWeb.back()
        } else {
            return super.onBackPressedSupport()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onResume() {
        super.onResume()
        agentWeb.webLifeCycle.onResume()
    }
}