package com.czl.lib_base.mvvm.ui

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.lib_base.BR
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.databinding.FragmentWebBinding
import com.czl.lib_base.mvvm.viewmodel.WebFmViewModel
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import com.just.agentweb.AgentWeb
import com.just.agentweb.NestedScrollAgentWebView
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient


@Route(path = AppConstants.Router.Base.F_WEB)
class WebFragment : BaseFragment<FragmentWebBinding, WebFmViewModel>() {

    private lateinit var agentWeb: AgentWeb
    private var errorFlag = false

    override fun initContentView(): Int {
        return R.layout.fragment_web
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun isThemeRedStatusBar(): Boolean {
        return true
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        val webView = NestedScrollAgentWebView(context)
        val lp = CoordinatorLayout.LayoutParams(-1, -1)
        lp.behavior = AppBarLayout.ScrollingViewBehavior()

        val url = arguments?.getString(AppConstants.BundleKey.WEB_URL)
        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.clWebRoot, 1, lp)
            .useDefaultIndicator(ContextCompat.getColor(requireContext(), R.color.md_theme_red), 1)
            .setWebView(webView)
            .interceptUnkownUrl()
            .setWebViewClient(object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    errorFlag = false
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    errorFlag = true
                    loadService.showWithConvertor(-1)
                }

                override fun onPageFinished(view: WebView, url: String?) {
                    super.onPageFinished(view, url)
                    if (!errorFlag) loadService.showWithConvertor(0)
//                    if (!view.canGoBack() && viewModel.ivToolbarIconRes.get() != 0) {
//                        viewModel.ivToolbarIconRes.set(0)
//                    }
//                    if (view.canGoBack() && viewModel.ivToolbarIconRes.get() != R.drawable.ic_delete_32dp) {
//                        viewModel.ivToolbarIconRes.set(R.drawable.ic_delete_32dp)
//                    }
                }
            })
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    viewModel.tvTitle.set(title)
                }
            })
            .createAgentWeb()
            .ready()
            .go(url)

    }

    override fun reload() {
        super.reload()
        agentWeb.urlLoader.reload()
    }

    override fun initViewObservable() {
        viewModel.uc.closeEvent.observe(this, {
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
        if (this::agentWeb.isInitialized)
            agentWeb.webLifeCycle.onResume()
    }

    override fun enableLazy(): Boolean {
        return false
    }
}