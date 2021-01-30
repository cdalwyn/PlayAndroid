package com.czl.lib_base.di

import com.czl.lib_base.base.AppManager
import com.czl.lib_base.base.MyApplication
import com.czl.lib_base.data.net.RetrofitClient
import com.czl.lib_base.base.AppViewModelFactory
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.bus.event.SingleLiveEvent
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.data.api.ApiService
import com.czl.lib_base.data.source.HttpDataSource
import com.czl.lib_base.data.source.LocalDataSource
import com.czl.lib_base.data.source.impl.HttpDataImpl
import com.czl.lib_base.data.source.impl.LocalDataImpl
import com.czl.lib_base.event.TokenExpiredEvent
import com.czl.lib_base.widget.AddTodoPopView
import com.czl.lib_base.widget.LoginPopView
import com.lxj.xpopup.XPopup
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * @author Alwyn
 * @Date 2020/8/5
 * @Description 注入的module
 */
val appModule = module {
    single { androidApplication() as MyApplication }
    // single->单例式  factory->每次都创建不同实例  viewModel->VM注入
    // androidApplication()->获取当前Application , androidContext() -> 获取context
    // 1 . 获取api实例
    single { RetrofitClient.getInstance().create(ApiService::class.java) }
    // 2. 创建实例前若构造方法内有参数 则需先注入构造中的参数实例
    single<HttpDataSource> { HttpDataImpl(get()) }
    // 3. 获取本地数据调用的实例
    single<LocalDataSource> { LocalDataImpl() }
    // 4 .综合以上本地+网络两个数据来源 得到数据仓库
    single { DataRepository(get(), get()) }
    // bind 将指定的实例绑定到对应的class  single { AppViewModelFactory(androidApplication(), get()) } bind TestActivity::class
    single { AppViewModelFactory(get(), get()) }

}
val factoryModule = module {
    factory(named("login")) {
        XPopup.Builder(AppManager.instance.currentActivity())
            .enableDrag(true)
            .moveUpToKeyboard(false)
            .autoOpenSoftInput(true)
            .asCustom(LoginPopView(AppManager.instance.currentActivity() as BaseActivity<*, *>))
    }
    factory(named("todo")) {
        XPopup.Builder(AppManager.instance.currentActivity())
            .enableDrag(true)
            .moveUpToKeyboard(true)
            .autoOpenSoftInput(true)
            .autoFocusEditText(true)
            .asCustom(AddTodoPopView(AppManager.instance.currentActivity() as BaseActivity<*, *>))
    }
}
val allModule = appModule + factoryModule
//val factoryModule = module {
//    // 带参数注入
//    factory { (view: View) -> TestDataImpl(view) }
//}

//val customModule = module {
//    // 绑定与TestActivity生命周期的作用域 通过lifecycleScope.inject<TestScopeDataImpl>()注入
//    scope<TestActivity> {
//        scoped { TestScopeDataImpl() }
//    }
//}

