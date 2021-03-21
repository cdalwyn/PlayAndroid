package com.czl.lib_base.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.czl.lib_base.R
import com.czl.lib_base.bus.Messenger
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.mvvm.ui.ContainerFmActivity
import com.czl.lib_base.route.RouteCenter
import com.czl.lib_base.util.DayModeUtil
import com.czl.lib_base.util.DialogHelper
import com.czl.lib_base.util.ToastHelper
import com.czl.lib_base.widget.ShareArticlePopView
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.pgyersdk.feedback.PgyerFeedbackManager
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import java.lang.reflect.ParameterizedType
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Alwyn on 2020/10/10.
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<*>> :
    BaseRxActivity(), IBaseView {
    protected lateinit var binding: V
    lateinit var viewModel: VM
    private var viewModelId = 0
    private var dialog: BasePopupView? = null
    private var rootBinding: ViewDataBinding? = null

    val dataRepository: DataRepository by inject()
    // 维护的未登录或登录过期后的登录弹窗组 避免并发请求多次弹出
    val loginPopMap: ConcurrentHashMap<Int,BasePopupView> by inject(named("login_map"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //页面接受的参数方法
        initParam()
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState)
        if (isImmersionBarEnabled())
            initStatusBar()
        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack()
        
        //页面数据初始化方法
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
    }

    open fun initStatusBar() {
        ImmersionBar.with(this).statusBarDarkFont(!DayModeUtil.isNightMode(this), 0.2f)
            .transparentStatusBar().init()
    }

    open fun isImmersionBarEnabled(): Boolean {
        return true
    }

    override fun onDestroy() {
        //解除Messenger注册 暂无业务使用到
//        Messenger.getDefault().unregister(viewModel)
        binding.unbind()
        rootBinding?.unbind()
        super.onDestroy()
    }

    // Fragment入场动画
    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultVerticalAnimator()
    }

    /**
     * 注入绑定
     */
    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        if (useBaseLayout()) {
            setContentView(R.layout.activity_base)
            val mActivityRoot = findViewById<ViewGroup>(R.id.activity_root)
            var parentContent: View = mActivityRoot
            // 绑定根布局
            rootBinding = DataBindingUtil.bind(parentContent)
            rootBinding?.setVariable(initVariableId(), initViewModel())
            rootBinding?.lifecycleOwner = this
            // 在根布局添加公共布局 目前只添加了标题栏
            if (addParentContentView() != 0) {
                parentContent = LayoutInflater.from(this).inflate(addParentContentView(), null)
                mActivityRoot.addView(parentContent)
            }
//            DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
            binding = DataBindingUtil.inflate(
                layoutInflater,
                initContentView(),
                parentContent as ViewGroup,
                true
            )
        } else {
            binding = DataBindingUtil.setContentView(this, initContentView())
        }
        viewModelId = initVariableId()
        viewModel = initViewModel()
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel)
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.lifecycleOwner = this
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel)
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this)
    }

    //刷新布局
    fun refreshLayout() {
        binding.setVariable(viewModelId, viewModel)
    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    private fun registerUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.uC.getShowLoadingEvent()
            .observe(this, { title: String? -> showLoading(title) })
        //加载对话框消失
        viewModel.uC.getDismissDialogEvent()
            .observe(this, { v: Void? -> dismissLoading() })
        //跳入新页面
        viewModel.uC.getStartActivityEvent().observe(
            this, { map ->
                val routePath: String = map[BaseViewModel.ParameterField.ROUTE_PATH] as String
                val bundle = map[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                RouteCenter.navigate(routePath, bundle)
            }
        )
        viewModel.uC.getStartFragmentEvent().observe(this, { map ->
            val routePath: String = map[BaseViewModel.ParameterField.ROUTE_PATH] as String
            val bundle: Bundle? = map[BaseViewModel.ParameterField.BUNDLE] as Bundle?
            start(RouteCenter.navigate(routePath, bundle) as SupportFragment)
        })
        //跳入ContainerActivity
        viewModel.uC.getStartContainerActivityEvent().observe(
            this, { params: Map<String?, Any?> ->
                val canonicalName = params[BaseViewModel.ParameterField.ROUTE_PATH] as String?
                val bundle = params[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                startContainerActivity(canonicalName, bundle)
            }
        )
        //关闭界面
        viewModel.uC.getFinishEvent().observe(this, {
            finish()
        })
        //关闭上一层
        viewModel.uC.getOnBackPressedEvent().observe(
            this, { onBackPressedSupport() }
        )
        // 弹出分享文章窗口
        viewModel.uC.getShowSharePopEvent().observe(this, {
            XPopup.Builder(this)
                .enableDrag(true)
                .moveUpToKeyboard(true)
                .autoOpenSoftInput(true)
                .asCustom(ShareArticlePopView(this, it))
                .show()
        })
    }

    fun showLoading(title: String?) {
        dialog = DialogHelper.showLoadingDialog(this, title)
    }

    fun dismissLoading() {
        dialog?.smartDismiss()
    }

    fun showErrorToast(msg: String?) {
        ToastHelper.showErrorToast(msg)
    }

    fun showNormalToast(msg: String?) {
        ToastHelper.showNormalToast(msg)
    }

    fun showSuccessToast(msg: String?) {
        ToastHelper.showSuccessToast(msg)
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>?) {
        startActivity(Intent(this, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 跳转容器页面
     * @param routePath Fragment路由地址
     * @param bundle    跳转所携带的信息
     */
    fun startContainerActivity(
        routePath: String?,
        bundle: Bundle? = null
    ) {
        val intent = Intent(this, ContainerFmActivity::class.java)
        intent.putExtra(ContainerFmActivity.FRAGMENT, routePath)
        if (bundle != null) {
            intent.putExtra(ContainerFmActivity.BUNDLE, bundle)
        }
        startActivity(intent)
    }

    override fun initParam() {}

    /**
     * @return 是否需要标题栏
     */
    protected open fun useBaseLayout(): Boolean {
        return true
    }

    /**
     * 添加根内容布局id（目前在xml内加了标题栏）
     *
     * @return
     */
    protected open fun addParentContentView(): Int {
        return 0
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun initContentView(): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    /**
     * 根据继承的泛型动态初始化ViewModel
     * 子类无须重写该方法 只需指定泛型即可
     * @return 继承BaseViewModel的ViewModel
     */
    private fun initViewModel(): VM {
        val type = javaClass.genericSuperclass
        val modelClass = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        return ViewModelProvider(this, get<AppViewModelFactory>()).get(modelClass)
    }

    override fun initData() {}

    override fun initViewObservable() {}

//    /**
//     * 创建ViewModel
//     *
//     * @param cls
//     * @param <T>
//     * @return
//     */
//    fun <T : ViewModel?> createViewModel(
//        activity: FragmentActivity?,
//        cls: Class<T>?
//    ): T {
//        return ViewModelProvider(activity!!).get(cls)
//    }
}