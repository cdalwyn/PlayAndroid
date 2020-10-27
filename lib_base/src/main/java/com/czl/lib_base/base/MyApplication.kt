package com.czl.lib_base.base

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.czl.lib_base.di.allModule
import com.tencent.mmkv.MMKV
import io.reactivex.plugins.RxJavaPlugins
import me.goldze.mvvmhabit.base.BaseApplication
import me.goldze.mvvmhabit.crash.CaocConfig
import com.blankj.utilcode.util.LogUtils
import com.czl.lib_base.BuildConfig
import com.czl.lib_base.util.switchicon.LauncherIconManager

import me.goldze.mvvmhabit.utils.ToastUtils
import me.yokeyword.fragmentation.Fragmentation
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG){
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        BaseApplication.setApplication(this)
        MMKV.initialize(this)
        // 初始化Fragmentation
        Fragmentation.builder()
            .stackViewMode(Fragmentation.BUBBLE)
            .debug(BuildConfig.DEBUG)
            .install()
        //是否开启日志打印
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG).setConsoleSwitch(BuildConfig.DEBUG)
        // 配置全局日志
        //LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG).setConsoleSwitch(BuildConfig.DEBUG)
        //配置全局异常崩溃操作
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
            .enabled(true) //是否启动全局异常捕获
            .showErrorDetails(BuildConfig.DEBUG) //是否显示错误详细信息
            .showRestartButton(true) //是否显示重启按钮
            .trackActivities(true) //是否跟踪Activity
            .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
//            .errorDrawable(R.mipmap.ic_launcher) //错误图标
//            .restartActivity(LoginActivity::class.java) //重新启动后的activity
            //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
            //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
            .apply()
        startKoin {
            androidContext(this@MyApplication)
            modules(allModule)
        }
        RxJavaPlugins.setErrorHandler {
            ToastUtils.showShort("系统错误")
            it.printStackTrace()
        }
        // 根据活动时间动态更换资源图标（如淘宝双11）
//        LauncherIconManager.register(this)
    }
}