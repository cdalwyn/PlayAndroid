package com.czl.lib_base.base

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.czl.lib_base.BuildConfig
import com.czl.lib_base.R
import com.czl.lib_base.callback.ErrorCallback
import com.czl.lib_base.config.AppConstants
import com.czl.lib_base.crash.CaocConfig
import com.czl.lib_base.data.DataRepository
import com.czl.lib_base.di.allModule
import com.czl.lib_base.util.DayModeUtil
import com.czl.lib_base.util.SpHelper
import com.czl.lib_base.util.ToastHelper
import com.didichuxing.doraemonkit.DoraemonKit
import com.didichuxing.doraemonkit.DoraemonKitReal
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import es.dmoral.toasty.Toasty
import io.reactivex.plugins.RxJavaPlugins
import me.jessyan.autosize.AutoSizeConfig
import me.yokeyword.fragmentation.Fragmentation
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.litepal.LitePal


/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
            //        DoraemonKit.apply {
//            setDebug(BuildConfig.DEBUG)
//            setAwaysShowMainIcon(BuildConfig.DEBUG)
//            install(this@MyApplication)
//        }
        }

        ARouter.init(this)
        setApplication(this)
        LitePal.initialize(this)
        MMKV.initialize(this)
        // 初始化Fragmentation
        Fragmentation.builder()
            .stackViewMode(Fragmentation.NONE)
            .debug(BuildConfig.DEBUG)
            .install()
        // 屏幕适配
        AutoSizeConfig.getInstance().setCustomFragment(true).setBaseOnWidth(false)
            .setExcludeFontScale(true).designHeightInDp = 720
        //是否开启日志打印
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG).setConsoleSwitch(BuildConfig.DEBUG)
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(allModule)
        }
        RxJavaPlugins.setErrorHandler {
            ToastHelper.showErrorToast("系统错误")
            it.printStackTrace()
        }
        // 设置吐司不以队列循环展示
        Toasty.Config.getInstance().allowQueue(false).apply()
        XPopup.setPrimaryColor(ContextCompat.getColor(this, R.color.md_theme_red))
        // 切换情景模式
        initNightMode()
    }

    private fun initNightMode() {
        if (SpHelper.decodeBoolean(AppConstants.SpKey.SYS_UI_MODE))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        else
            AppCompatDelegate.setDefaultNightMode(
                if (SpHelper.decodeBoolean(AppConstants.SpKey.USER_UI_MODE)) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
    }

    companion object {
        init {
            ClassicsFooter.REFRESH_FOOTER_FINISH = ""
            SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
                layout.apply {
                    setEnableOverScrollDrag(true)
                    setEnableScrollContentWhenLoaded(false)
                    setEnableAutoLoadMore(true)
                    setEnableOverScrollBounce(true)
                    setFooterHeight(60f)
                }
            }
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.apply {
                    setPrimaryColorsId(R.color.md_theme_red, R.color.white)
                }
                MaterialHeader(context).setColorSchemeColors(
                    ContextCompat.getColor(
                        context,
                        R.color.md_theme_red
                    )
                )
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                ClassicsFooter(context).setFinishDuration(0)
            }
        }
    }

    private fun setApplication(application: Application) {
        //初始化工具类
        Utils.init(application)
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(object :
            ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
                AppManager.instance.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
            override fun onActivityDestroyed(activity: Activity) {
                AppManager.instance.removeActivity(activity)
            }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

}