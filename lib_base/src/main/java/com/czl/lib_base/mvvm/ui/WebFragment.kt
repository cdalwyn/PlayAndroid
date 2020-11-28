package com.czl.lib_base.mvvm.ui

import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.BR
import com.czl.lib_base.R
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.databinding.FragmentWebBinding
import com.czl.lib_base.mvvm.viewmodel.WebFmViewModel
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.NestedScrollAgentWebView
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient


@Route(path = AppConstants.Router.Base.F_WEB)
class WebFragment : BaseFragment<FragmentWebBinding, WebFmViewModel>() {

    private lateinit var agentWeb: AgentWeb

    // 是否加载失败
    private var errorFlag = false

    // 当前web标题
    private var currentTitle: String? = null

    // 当前web链接
    private var currentLink: String? = null

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
        initWebView()
    }

    override fun initViewObservable() {
        viewModel.uc.closeEvent.observe(this, {
            requireActivity().finish()
        })
        viewModel.uc.collectEvent.observe(this, {
            if (errorFlag) {
                showNormalToast("当前页面加载错误,收藏失败")
                return@observe
            }
            if (!viewModel.collectFlag.get()!!) viewModel.collectWebsite(currentTitle, currentLink)
            else viewModel.unCollectWebsite(arguments?.getString(AppConstants.BundleKey.WEB_URL_ID))
        })
        viewModel.uc.goForwardEvent.observe(this, {
            if (viewModel.canForwardFlag.get()!!) agentWeb.webCreator.webView.goForward()
        })
    }

    private fun initWebView() {
        val webView = NestedScrollAgentWebView(context)
        val lp = CoordinatorLayout.LayoutParams(-1, -1)
        lp.behavior = AppBarLayout.ScrollingViewBehavior()
        val url = arguments?.getString(AppConstants.BundleKey.WEB_URL)
        viewModel.collectFlag.set(arguments?.getBoolean(AppConstants.BundleKey.WEB_URL_COLLECT_FLAG))
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
                    request: WebResourceRequest,
                    error: WebResourceError
                ) {
                    super.onReceivedError(view, request, error)
                    if (request.isForMainFrame) {
                        errorFlag = true
                        loadService.showWithConvertor(-1)
                    }
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        return
                    }
                    errorFlag = true
                    loadService.showWithConvertor(-1)
                }

                override fun onReceivedHttpError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    errorResponse: WebResourceResponse?
                ) {
                    super.onReceivedHttpError(view, request, errorResponse)
                    val statusCode = errorResponse?.statusCode
                    if (404 == statusCode || 500 == statusCode) {
                        errorFlag = true
                        loadService.showWithConvertor(-1)
                    }
                }

                override fun onPageFinished(view: WebView, url: String?) {
                    super.onPageFinished(view, url)
                    currentLink = url
                    if (!errorFlag) {
                        loadService.showWithConvertor(0)
                    }
                    viewModel.canGoBackFlag.set(view.canGoBack())
                    viewModel.canForwardFlag.set(view.canGoForward())
                }
            })
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String) {
                    super.onReceivedTitle(view, title)
                    viewModel.tvTitle.set(title)
                    this@WebFragment.currentTitle = title
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