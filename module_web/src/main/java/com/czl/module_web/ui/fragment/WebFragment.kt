package com.czl.module_web.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.czl.lib_base.BR
import com.czl.lib_base.base.BaseFragment
import com.czl.lib_base.config.AppConstants
import com.czl.module_web.R
import com.czl.module_web.databinding.WebFragmentWebBinding
import com.czl.module_web.viewmodel.WebFmViewModel
import com.czl.module_web.widget.WebMenuPop
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.*
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.lxj.xpopup.XPopup
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


@Route(path = AppConstants.Router.Web.F_WEB)
class WebFragment : BaseFragment<WebFragmentWebBinding, WebFmViewModel>() {

    var agentWeb: AgentWeb? = null

    // 是否加载失败
    private var errorFlag = false

    // 当前web标题
    var currentTitle: String? = null

    // 当前web链接
    var currentLink: String? = null

    // 首页链接
    var homeUrl: String? = null

    // 首页标题
    var homeTitle: String? = null

    override fun initContentView(): Int {
        return R.layout.web_fragment_web
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
            clearEditFocus()
            viewModel.collectWebsite(currentTitle, currentLink)
//            if (!viewModel.collectFlag.get()!!) viewModel.collectWebsite(currentTitle, currentLink)
//            else viewModel.unCollectWebsite(arguments?.getString(AppConstants.BundleKey.WEB_URL_ID))
        })
        viewModel.uc.goForwardEvent.observe(this, {
            if (viewModel.canForwardFlag.get()!!) agentWeb?.webCreator?.webView?.goForward()
        })
        viewModel.uc.showMenuEvent.observe(this, {
            XPopup.Builder(context)
                .enableDrag(true)
                .asCustom(WebMenuPop(this))
                .show()
        })
        viewModel.uc.openBrowserEvent.observe(this, {
            clearEditFocus()
            val uri = Uri.parse(currentLink)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        })
        viewModel.uc.copyCurrentLinkEvent.observe(this, {
            ClipboardUtils.copyText(currentLink)
            showSuccessToast("复制成功")
            clearEditFocus()
        })
    }

    private fun clearEditFocus() {
        binding.etWeb.apply {
            clearFocus()
            KeyboardUtils.hideSoftInput(this)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val webView = NestedScrollAgentWebView(context)
        val lp = CoordinatorLayout.LayoutParams(-1, -1)
        lp.behavior = AppBarLayout.ScrollingViewBehavior()
        homeUrl = arguments?.getString(AppConstants.BundleKey.WEB_URL)
        viewModel.collectFlag.set(arguments?.getBoolean(AppConstants.BundleKey.WEB_URL_COLLECT_FLAG))
        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.clWebRoot, 1, lp)
            .useDefaultIndicator(ContextCompat.getColor(requireContext(), R.color.md_theme_red), 1)
            .setWebView(webView)
            .interceptUnkownUrl()
            .setWebViewClient(mWebClient)
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView, title: String) {
                    super.onReceivedTitle(view, title)
                    viewModel.tvTitle.set(title)
                    if (currentLink == homeUrl && !view.canGoBack() && !errorFlag) {
                        this@WebFragment.homeTitle = title
                        // 保存到本地
                        if (!TextUtils.isEmpty(homeUrl) && !TextUtils.isEmpty(homeTitle)) {
                            viewModel.saveBrowseHistory(homeTitle!!, homeUrl!!)
                        }
                    }
                    this@WebFragment.currentTitle = title
                }
            })
            .createAgentWeb()
            .ready()
            .go(homeUrl)
        webView.settings.apply {
            //支持javascript
            javaScriptEnabled = true
            // 设置可以支持缩放
            setSupportZoom(true)
            // 设置出现缩放工具
            builtInZoomControls = true
            //扩大比例的缩放
            useWideViewPort = true
            //自适应屏幕
            loadWithOverviewMode = true
            displayZoomControls = false
        }
    }

    override fun reload() {
        super.reload()
        agentWeb?.urlLoader?.reload()
    }

    override fun back() {
        if (!viewModel.canGoBackFlag.get()!!) {
            super.back()
        } else {
            agentWeb?.back()
        }
    }

    override fun onBackPressedSupport(): Boolean {
        if (agentWeb!!.webCreator.webView.canGoBack()) {
            agentWeb!!.back()
        } else {
            return super.onBackPressedSupport()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb?.webLifeCycle?.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        agentWeb?.webLifeCycle?.onPause()
    }

    override fun onResume() {
        super.onResume()
        agentWeb?.webLifeCycle?.onResume()
    }

    override fun enableLazy(): Boolean {
        return false
    }

    private val mWebClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            errorFlag = false
            currentLink = url
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(DefaultWebClient.INTENT_SCHEME) || url.endsWith(".apk")) {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }


        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {
            val url = request.url.toString()
            if (url.startsWith(DefaultWebClient.INTENT_SCHEME) || url.endsWith(".apk")
            ) {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }

            return super.shouldOverrideUrlLoading(view, request)
        }


        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
            if (request.isForMainFrame) {
                errorFlag = true
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
                showErrorStatePage()
            }
        }

        override fun onPageFinished(view: WebView, url: String?) {
            super.onPageFinished(view, url)
            if (!errorFlag) {
                showSuccessStatePage()
            }
            viewModel.canForwardFlag.set(view.canGoForward())
            if (currentLink == homeUrl && !view.canGoBack()) {
                viewModel.canGoBackFlag.set(false)
                return
            }
            viewModel.canGoBackFlag.set(view.canGoBack())
        }
    }
}