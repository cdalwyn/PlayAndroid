package com.czl.lib_base.extension

import android.annotation.SuppressLint
import android.net.ParseException
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.czl.lib_base.base.AppManager
import com.czl.lib_base.base.BaseActivity
import com.czl.lib_base.base.BaseBean
import com.czl.lib_base.util.ToastHelper.showErrorToast
import com.czl.lib_base.widget.LoginPopView
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.lxj.xpopup.XPopup
import io.reactivex.observers.DisposableObserver
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author Alwyn
 * @Date 2020/10/10
 * @Description RxJava 处理Api异常
 */
abstract class ApiSubscriberHelper<T> : DisposableObserver<T>() {
    override fun onNext(t: T) {
        if (t is BaseBean<*> && t.errorCode != 0) {
            showErrorToast(t.errorMsg)
            if (t.errorCode == -1001) {
                LogUtils.e("当前用户未登录或者登录已失效")
                XPopup.Builder(AppManager.instance.currentActivity())
                    .enableDrag(true)
                    .moveUpToKeyboard(false)
                    .autoOpenSoftInput(true)
                    .isDestroyOnDismiss(true)
                    .asCustom(LoginPopView(AppManager.instance.currentActivity() as BaseActivity<*, *>))
                    .show()
            }
        }
        onResult(t)
    }

    override fun onComplete() {}

    @SuppressLint("MissingPermission")
    override fun onError(throwable: Throwable) {
        if (!NetworkUtils.isConnected() || throwable is ConnectTimeoutException) {
            onFailed("连接失败，请检查网络后再试")
        } else if (throwable is RuntimeException) {
            onFailed(throwable.message)
        } else if (throwable is SocketTimeoutException) {
            onFailed("连接超时，请重试")
        } else if (throwable is IllegalStateException) {
            onFailed(throwable.message)
        } else if (throwable is HttpException) {
            onFailed("网络异常，请重试")
        } else if (throwable is JsonParseException
            || throwable is JSONException
            || throwable is JsonSyntaxException
            || throwable is ParseException
        ) {
            onFailed("数据解析异常，请稍候再试")
        } else if (throwable is UnknownHostException) {
            onFailed("连接失败，请检查网络后再试")
        } else {
            onFailed(throwable.message)
        }
    }

    protected abstract fun onResult(t: T)
    protected abstract fun onFailed(msg: String?)
}