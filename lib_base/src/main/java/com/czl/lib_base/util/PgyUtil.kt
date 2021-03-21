package com.czl.lib_base.util

import android.content.Context
import com.blankj.utilcode.util.Utils
import com.czl.lib_base.base.AppManager
import com.czl.lib_base.mvvm.ui.ContainerFmActivity
import com.pgyersdk.feedback.PgyerFeedbackManager
import com.pgyersdk.update.PgyUpdateManager
import com.pgyersdk.update.UpdateManagerListener
import com.pgyersdk.update.javabean.AppBean
import java.lang.Exception

/**
 * @author Alwyn
 * @Date 2021/3/21
 * @Description
 */
object PgyUtil {
    fun checkVersion(context: Context) {
        PgyUpdateManager.Builder()
            .setForced(false)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
            .setUserCanRetry(true)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
            .setDeleteHistroyApk(true)
            .setUpdateManagerListener(object : UpdateManagerListener {
                override fun onNoUpdateAvailable() {
                    // 无更新
                    if (AppManager.instance.currentActivity() is ContainerFmActivity)
                        ToastHelper.showNormalToast("已是最新版本了")
                }

                override fun onUpdateAvailable(appBean: AppBean) {
                    // 有更新
                    DialogHelper.showVersionDialog(context, "发现新版本", appBean.releaseNote) {
                        PgyUpdateManager.downLoadApk(appBean.downloadURL)
                    }
                }

                override fun checkUpdateFailed(e: Exception) {
                    // 检查版本失败
                    ToastHelper.showErrorToast("检查更新失败")
                }

            })
            .register()
    }

    fun showFeedback() {
        PgyerFeedbackManager.PgyerFeedbackBuilder().builder().invoke()
    }
}