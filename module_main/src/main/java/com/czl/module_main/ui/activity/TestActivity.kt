package com.czl.module_main.ui.activity

import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.permissionx.guolindev.callback.RequestCallback
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.event.LiveBusCenter
import com.czl.lib_base.util.PermissionUtil
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.databinding.MainLayoutTestBinding
import com.czl.module_main.viewmodel.TestViewModel


/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
@Route(path = AppConstants.Router.Main.A_TEST)
class TestActivity : BaseActivity<MainLayoutTestBinding, TestViewModel>() {
    /*  // VM注入
      private val vm: TestViewModel by viewModel()
      // 绑定与该Activity的生命周期
      private val scopeDataImpl by lifecycleScope.inject<TestScopeDataImpl>()
      // 带参数注入
      private val testParamInject by inject<TestDataImpl> { parametersOf(binding.btnInject) }*/

    override fun initContentView(): Int {
        return R.layout.main_layout_test
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    /*override fun initViewModel(): TestViewModel {
        // 使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用XXXViewModel(@NonNull Application application)构造方法，
        // 如需调用M层则重写该方法 并在AppViewModelFactory类中create()方法创建或者使用依赖注入的方式
        // val factory = AppViewModelFactory.getInstance(application)
        // 直接使用koin注入的方式
        val factory: AppViewModelFactory = get()
        return ViewModelProvider(this, factory).get(TestViewModel::class.java)
    }*/

    override fun initViewObservable() {
        // 列表item点击事件的观察者
        viewModel.uc.deleteItemLiveData.observe(this, Observer {
            val index = viewModel.getItemPosition(it)
        })
        // 接收通信事件
        LiveBusCenter.observeMainEvent(this) {
            showNormalToast("MainActivity收到消息：${it.msg}")
        }
    }

    override fun initData() {
        showNormalToast(
            if (viewModel.getLoginUserName().isNullOrBlank())
                "当前尚未登录" else "当前用户${viewModel.getLoginUserName()}已登录"
        )
        viewModel.tvTitle.set("首页")
        viewModel.btnBackVisibility.set("0")
        PermissionUtil.reqStorage(
            this, callback = RequestCallback { allGranted, _, _ ->
                if (allGranted) {
                    LogUtils.i("存储权限授予成功")
                }
            })
    }
}